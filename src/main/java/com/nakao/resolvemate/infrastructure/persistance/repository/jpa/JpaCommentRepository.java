package com.nakao.resolvemate.infrastructure.persistance.repository.jpa;

import com.nakao.resolvemate.infrastructure.persistance.entity.CommentEntity;
import com.nakao.resolvemate.infrastructure.persistance.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaCommentRepository extends JpaRepository<CommentEntity, UUID> {

    List<CommentEntity> findByTicket(TicketEntity ticket);

}
