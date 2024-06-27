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
import java.util.Optional;
import java.util.UUID;
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
    public List<Comment> findAllByTicket(Ticket ticket) {
        return repository.findAllByTicket(TicketMapper.toEntity(ticket)).stream()
                .map(CommentMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Comment> findById(UUID id) {
        return repository.findById(id).map(CommentMapper::toModel);
    }

    @Override
    public boolean hasAccessToComment(UUID commentId, UUID userId) {
        return repository.hasAccessToComment(commentId, userId);
    }

}
