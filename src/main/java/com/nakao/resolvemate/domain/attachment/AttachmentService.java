package com.nakao.resolvemate.domain.attachment;

import com.nakao.resolvemate.domain.comment.Comment;
import com.nakao.resolvemate.domain.comment.CommentRepository;
import com.nakao.resolvemate.domain.exception.FileHandlingErrorException;
import com.nakao.resolvemate.domain.exception.FileSizeLimitExceededException;
import com.nakao.resolvemate.domain.exception.ResourceNotFoundException;
import com.nakao.resolvemate.domain.exception.ForbiddenAccessException;
import com.nakao.resolvemate.domain.user.Role;
import com.nakao.resolvemate.domain.user.User;
import com.nakao.resolvemate.domain.util.FileCompressionService;
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

    @Value("${app.file.max-size}")
    private Long MAX_FILE_SIZE;

    public AttachmentDTO createAttachment(UUID commentId, MultipartFile file) {
        Comment comment = getCurrentComment(commentId);
        verifyAuthorization(commentId);
        verifyFileSize(file.getSize());

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
            throw new FileHandlingErrorException("Error uploading file for comment " + commentId);
        }
    }

    public List<AttachmentDTO> getAttachmentsByCommentId(UUID commentId) {
        Comment comment = getCurrentComment(commentId);
        verifyAuthorization(commentId);

        return attachmentRepository.findAllByComment(comment).stream()
                .map(attachment -> {
                    byte[] decompressedData = FileCompressionService.decompressData(attachment.getData());
                    AttachmentDTO dto = AttachmentMapper.toDTO(attachment);
                    dto.setData(decompressedData);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private void verifyAuthorization(UUID commentId) {
        User currentUser = securityService.getAuthenticatedUser();

        if (!commentRepository.hasAccessToComment(commentId, currentUser.getId()) &&
                !Objects.equals(currentUser.getRole(), Role.ADMIN)) {
            throw new ForbiddenAccessException("Unauthorized access attempt by user " + currentUser.getId() + " to comment " + commentId);
        }
    }

    private void verifyFileSize(Long fileSize) {
        if (fileSize > MAX_FILE_SIZE) {
            throw new FileSizeLimitExceededException("The file is too large. Max size is " + MAX_FILE_SIZE + " bytes");
        }
    }

    private Comment getCurrentComment(UUID commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with ID: " + commentId));
    }

}
