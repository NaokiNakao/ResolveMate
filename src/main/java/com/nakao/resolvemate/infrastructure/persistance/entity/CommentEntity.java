package com.nakao.resolvemate.infrastructure.persistance.entity;

import com.nakao.resolvemate.infrastructure.audit.Auditable;
import com.nakao.resolvemate.infrastructure.audit.AuditingEntityListener;
import jakarta.persistence.*;
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
@Entity
@Table(name = "comments")
@EntityListeners(AuditingEntityListener.class)
public class CommentEntity implements Auditable {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private TicketEntity ticket;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Override
    public void setCreatedDate(Date createdDate) {
        this.createdAt = createdDate;
    }

    @Override
    public void setCreatedBy(UserEntity createdBy) {
        this.user = createdBy;
    }

}
