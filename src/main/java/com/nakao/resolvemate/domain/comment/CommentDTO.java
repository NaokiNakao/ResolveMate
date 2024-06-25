package com.nakao.resolvemate.domain.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private UUID id;
    private UUID ticketId;
    private UUID userId;
    private String content;
    private Date createdAt;
}
