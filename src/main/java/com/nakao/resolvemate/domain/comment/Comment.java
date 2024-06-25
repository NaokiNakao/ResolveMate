package com.nakao.resolvemate.domain.comment;

import com.nakao.resolvemate.domain.ticket.Ticket;
import com.nakao.resolvemate.domain.user.User;
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
public class Comment {
    private UUID id;
    private Ticket ticket;
    private User createdBy;
    private String content;
    private Date createdAt;
}
