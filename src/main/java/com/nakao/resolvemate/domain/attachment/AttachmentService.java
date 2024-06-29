package com.nakao.resolvemate.domain.attachment;

import com.nakao.resolvemate.domain.comment.Comment;
import com.nakao.resolvemate.domain.comment.CommentRepository;
import com.nakao.resolvemate.domain.exception.FileHandlingException;
import com.nakao.resolvemate.domain.exception.FileSizeLimitExceededException;
import com.nakao.resolvemate.domain.exception.ResourceNotFoundException;
import com.nakao.resolvemate.domain.exception.ForbiddenAccessException;
import com.nakao.resolvemate.domain.user.Role;
import com.nakao.resolvemate.domain.user.User;
import com.nakao.resolvemate.domain.util.FileCompressionService;
import com.nakao.resolvemate.domain.util.LogService;
import com.nakao.resolvemate.domain.util.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    private final LogService<AttachmentService> logService;

    @Value("${app.file.max-size}")
    private Long MAX_FILE_SIZE;

    /**
     * Creates a new attachment for a given comment.
     *
     * @param commentId the ID of the comment to which the attachment will be added
     * @param file the file to be attached
     * @return the created AttachmentDTO
     * @throws ResourceNotFoundException if the comment is not found
     * @throws FileHandlingException if there is an error uploading the file
     */
    public AttachmentDTO createAttachment(UUID commentId, MultipartFile file) {
        Comment comment = getCurrentComment(commentId);

        try {
            byte[] compressedData = FileCompressionService.compressData(file.getBytes());

            Attachment attachment = Attachment.builder()
                    .comment(comment)
                    .fileName(file.getOriginalFilename())
                    .fileType(file.getContentType())
                    .data(compressedData)
                    .build();

            AttachmentDTO createdAttachment = AttachmentMapper.toDTO(attachmentRepository.save(attachment));
            logService.info(this, "Attachment created: " + commentId);
            return createdAttachment;
        } catch (IOException e) {
            String message = "Error uploading file for comment ID: " + commentId;
            logService.error(this, message);
            throw new FileHandlingException(message);
        }
    }

    /**
     * Retrieves all attachments for a given comment and decompresses the data.
     *
     * @param commentId the ID of the comment whose attachments are to be retrieved
     * @return a list of AttachmentDTOs
     * @throws ResourceNotFoundException if the comment is not found
     */
    public List<AttachmentDTO> getAttachmentsByCommentId(UUID commentId) {
        Comment comment = getCurrentComment(commentId);

        List<AttachmentDTO> attachments = attachmentRepository.findAllByComment(comment).stream()
                .map(attachment -> {
                    byte[] decompressedData = FileCompressionService.decompressData(attachment.getData());
                    AttachmentDTO dto = AttachmentMapper.toDTO(attachment);
                    dto.setData(decompressedData);
                    return dto;
                })
                .collect(Collectors.toList());

        logService.info(this, "Found " + attachments.size() + " attachments for comment ID: " + commentId);
        return attachments;
    }

    /**
     * Verifies if the authenticated user has authorization to access the specified comment.
     *
     * @param commentId the ID of the comment to check authorization against
     * @throws ForbiddenAccessException if the user does not have access to the comment
     */
    public void verifyAuthorization(UUID commentId) {
        User currentUser = securityService.getAuthenticatedUser();

        if (!commentRepository.hasAccessToComment(commentId, currentUser.getId()) &&
                !Objects.equals(currentUser.getRole(), Role.ADMIN)) {
            logService.warn(this, "Unauthorized access attempt by user ID: " + currentUser.getId() + " to comment ID: " + commentId);
            throw new ForbiddenAccessException("Unauthorized access");
        }
    }

    /**
     * Verifies if the given file size exceeds the maximum allowed file size.
     *
     * @param fileSize the size of the file to verify, in bytes
     * @throws FileSizeLimitExceededException if the file size exceeds the maximum allowed size
     */
    public void verifyFileSize(Long fileSize) {
        if (fileSize > MAX_FILE_SIZE) {
            String message = "The file is too large. Max size is " + MAX_FILE_SIZE + " bytes";
            logService.warn(this, message);
            throw new FileSizeLimitExceededException(message);
        }
    }

    private Comment getCurrentComment(UUID commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> {
                    String message = "Comment not found with ID: " + commentId;
                    logService.warn(this, message);
                    return new ResourceNotFoundException(message);
                });
    }

}
