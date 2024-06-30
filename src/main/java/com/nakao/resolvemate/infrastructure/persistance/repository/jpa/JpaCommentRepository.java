package com.nakao.resolvemate.infrastructure.persistance.repository.jpa;

import com.nakao.resolvemate.infrastructure.persistance.entity.CommentEntity;
import com.nakao.resolvemate.infrastructure.persistance.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface JpaCommentRepository extends JpaRepository<CommentEntity, UUID> {

    List<CommentEntity> findAllByTicket(TicketEntity ticket);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM CommentEntity c JOIN c.ticket t JOIN UserEntity u ON (t.customer.id = u.id OR t.supportAgent.id = u.id) " +
            "WHERE c.id = :commentId AND u.id = :userId")
    boolean hasAccessToComment(@Param("commentId") UUID commentId, @Param("userId") UUID userId);

}
