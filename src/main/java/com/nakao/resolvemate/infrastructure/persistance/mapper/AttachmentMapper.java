package com.nakao.resolvemate.infrastructure.persistance.mapper;

import com.nakao.resolvemate.domain.attachment.Attachment;
import com.nakao.resolvemate.infrastructure.persistance.entity.AttachmentEntity;

public class AttachmentMapper {

    public static Attachment toModel(AttachmentEntity entity) {
        return Attachment.builder()
                .id(entity.getId())
                .comment(CommentMapper.toModel(entity.getComment()))
                .fileName(entity.getFileName())
                .fileType(entity.getFileType())
                .data(entity.getData())
                .build();
    }

    public static AttachmentEntity toEntity(Attachment attachment) {
        return AttachmentEntity.builder()
                .id(attachment.getId())
                .comment(CommentMapper.toEntity(attachment.getComment()))
                .fileName(attachment.getFileName())
                .fileType(attachment.getFileType())
                .data(attachment.getData())
                .build();
    }

}
