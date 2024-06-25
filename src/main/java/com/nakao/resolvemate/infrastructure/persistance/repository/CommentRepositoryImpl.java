package com.nakao.resolvemate.infrastructure.persistance.repository;

import com.nakao.resolvemate.domain.comment.Comment;
import com.nakao.resolvemate.domain.comment.CommentRepository;
import com.nakao.resolvemate.domain.ticket.Ticket;
import com.nakao.resolvemate.infrastructure.persistance.entity.CommentEntity;
import com.nakao.resolvemate.infrastructure.persistance.mapper.CommentMapper;
import com.nakao.resolvemate.infrastructure.persistance.mapper.TicketMapper;
import com.nakao.resolvemate.infrastructure.persistance.repository.jpa.JpaCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {

    private final JpaCommentRepository repository;

    @Override
    public Comment save(Comment comment) {
        CommentEntity commentEntity = CommentMapper.toEntity(comment);
        return CommentMapper.toModel(repository.save(commentEntity));
    }

    @Override
    public List<Comment> findByTicket(Ticket ticket) {
        return repository.findByTicket(TicketMapper.toEntity(ticket)).stream()
                .map(CommentMapper::toModel)
                .collect(Collectors.toList());
    }

}
