package com.nakao.resolvemate.infrastructure.persistance.entity;

import com.nakao.resolvemate.domain.ticket.Priority;
import com.nakao.resolvemate.domain.ticket.Status;
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
@Table(name = "tickets")
@EntityListeners(AuditingEntityListener.class)
public class TicketEntity implements Auditable {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private UserEntity customer;

    @ManyToOne
    @JoinColumn(name = "support_agent_id")
    private UserEntity supportAgent;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Override
    public void setCreatedDate(Date createdDate) {
        this.createdAt = createdDate;
    }

    @Override
    public void setCreatedBy(UserEntity createdBy) {
        this.customer = createdBy;
    }

}
