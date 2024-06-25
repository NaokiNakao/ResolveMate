package com.nakao.resolvemate.domain.comment;

public class CommentMapper {

    public static CommentDTO toDTO(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .ticketId(comment.getTicket().getId())
                .userId(comment.getUser().getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }

}
