package com.nakao.resolvemate.domain.comment;

import com.nakao.resolvemate.domain.ticket.Ticket;

import java.util.List;

public interface CommentRepository {

    Comment save(Comment comment);

    List<Comment> findByTicket(Ticket ticket);

}
