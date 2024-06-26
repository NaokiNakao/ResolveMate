package com.nakao.resolvemate.domain.attachment;

public class AttachmentMapper {

    public static AttachmentDTO toDTO(Attachment attachment) {
        return AttachmentDTO.builder()
                .id(attachment.getId())
                .commentId(attachment.getComment().getId())
                .fileName(attachment.getFileName())
                .fileType(attachment.getFileType())
                .data(attachment.getData())
                .build();
    }

}
