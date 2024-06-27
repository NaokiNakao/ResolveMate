package com.nakao.resolvemate.domain.attachment;

import com.nakao.resolvemate.domain.comment.Comment;
import com.nakao.resolvemate.domain.comment.CommentRepository;
import com.nakao.resolvemate.domain.exception.FileHandlingException;
import com.nakao.resolvemate.domain.exception.FileSizeLimitExceededException;
import com.nakao.resolvemate.domain.exception.ResourceNotFoundException;
import com.nakao.resolvemate.domain.exception.UnauthorizedAccessException;
import com.nakao.resolvemate.domain.user.Role;
import com.nakao.resolvemate.domain.user.User;
import com.nakao.resolvemate.domain.util.FileCompressionService;
import com.nakao.resolvemate.domain.util.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final CommentRepository commentRepository;
    private final SecurityService securityService;

    @Value("${app.file.max-size}")
    private Long MAX_FILE_SIZE;

    /**
     * Creates a new attachment for a given comment.
     *
     * @param commentId the ID of the comment to which the attachment will be added
     * @param file the file to be attached
     * @return the created AttachmentDTO
     * @throws ResourceNotFoundException if the comment is not found
     * @throws FileSizeLimitExceededException if the file size exceeds the maximum allowed size
     * @throws FileHandlingException if there is an error uploading the file
     */
    @CacheEvict(value = "attachments", key = "#commentId")
    public AttachmentDTO createAttachment(UUID commentId, MultipartFile file) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new FileSizeLimitExceededException("The file is too large. Max size is " + MAX_FILE_SIZE + " bytes");
        }

        try {
            byte[] compressedData = FileCompressionService.compressData(file.getBytes());

            Attachment attachment = Attachment.builder()
                    .comment(comment)
                    .fileName(file.getOriginalFilename())
                    .fileType(file.getContentType())
                    .data(compressedData)
                    .build();

            return AttachmentMapper.toDTO(attachmentRepository.save(attachment));
        } catch (IOException e) {
            throw new FileHandlingException("Error uploading file");
        }
    }

    /**
     * Retrieves all attachments for a given comment and decompresses the data.
     *
     * @param commentId the ID of the comment whose attachments are to be retrieved
     * @return a list of AttachmentDTOs
     * @throws ResourceNotFoundException if the comment is not found
     */
    @Cacheable(value = "attachments", key = "#commentId")
    public List<AttachmentDTO> getAttachmentsByCommentId(UUID commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));

        return attachmentRepository.findAllByComment(comment).stream()
                .map(attachment -> {
                    byte[] decompressedData = FileCompressionService.decompressData(attachment.getData());
                    AttachmentDTO dto = AttachmentMapper.toDTO(attachment);
                    dto.setData(decompressedData);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Verifies if the authenticated user has authorization to access the specified comment.
     *
     * @param commentId the ID of the comment to check authorization against
     * @throws UnauthorizedAccessException if the user does not have access to the comment
     */
    public void verifyAuthorization(UUID commentId) {
        User currentUser = securityService.getAuthenticatedUser();

        if (!commentRepository.hasAccessToComment(commentId, currentUser.getId()) &&
                !Objects.equals(currentUser.getRole(), Role.ADMIN)) {
            throw new UnauthorizedAccessException("Unauthorized access");
        }
    }

}
