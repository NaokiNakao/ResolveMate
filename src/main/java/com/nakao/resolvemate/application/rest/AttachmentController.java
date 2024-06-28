package com.nakao.resolvemate.application.rest;

import com.nakao.resolvemate.domain.attachment.AttachmentDTO;
import com.nakao.resolvemate.domain.attachment.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/attachments")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

    @PostMapping("/{commentId}")
    public ResponseEntity<AttachmentDTO> createAttachment(@PathVariable UUID commentId,
                                                          @RequestParam("file") MultipartFile file) {

        attachmentService.verifyAuthorization(commentId);
        attachmentService.verifyFileSize(file.getSize());
        AttachmentDTO createdAttachment = attachmentService.createAttachment(commentId, file);
        return new ResponseEntity<>(createdAttachment, HttpStatus.CREATED);
    }

    @GetMapping("/{commentId}")
    private ResponseEntity<List<AttachmentDTO>> getAttachmentsByCommentId(@PathVariable UUID commentId) {
        attachmentService.verifyAuthorization(commentId);
        List<AttachmentDTO> attachments = attachmentService.getAttachmentsByCommentId(commentId);
        return new ResponseEntity<>(attachments, HttpStatus.OK);
    }

}
