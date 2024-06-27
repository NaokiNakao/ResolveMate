package com.nakao.resolvemate.domain.comment;

import com.nakao.resolvemate.domain.ticket.Ticket;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentRepository {

    Comment save(Comment comment);

    List<Comment> findAllByTicket(Ticket ticket);

    Optional<Comment> findById(UUID id);

    boolean hasAccessToComment(UUID commentId, UUID userId);

}
