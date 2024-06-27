package com.nakao.resolvemate.infrastructure.persistance.mapper;

import com.nakao.resolvemate.domain.comment.Comment;
import com.nakao.resolvemate.infrastructure.persistance.entity.CommentEntity;

public class CommentMapper {

    public static Comment toModel(CommentEntity entity) {
        return Comment.builder()
                .id(entity.getId())
                .ticket(TicketMapper.toModel(entity.getTicket()))
                .user(UserMapper.toModel(entity.getUser()))
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public static CommentEntity toEntity(Comment comment) {
        return CommentEntity.builder()
                .id(comment.getId())
                .ticket(TicketMapper.toEntity(comment.getTicket()))
                .user(comment.getUser() != null ? UserMapper.toEntity(comment.getUser()) : null)
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }

}
