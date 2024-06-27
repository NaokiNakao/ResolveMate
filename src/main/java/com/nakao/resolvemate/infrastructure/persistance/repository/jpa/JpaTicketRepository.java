package com.nakao.resolvemate.infrastructure.persistance.repository.jpa;

import com.nakao.resolvemate.infrastructure.persistance.entity.TicketEntity;
import com.nakao.resolvemate.infrastructure.persistance.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface JpaTicketRepository extends JpaRepository<TicketEntity, UUID> {

    int countBySupportAgent(UserEntity supportAgent);

    List<TicketEntity> findAllByCustomer(UserEntity customer);

    List<TicketEntity> findAllBySupportAgent(UserEntity supportAgent);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END " +
            "FROM TicketEntity t JOIN UserEntity u ON (t.customer.id = u.id OR t.supportAgent.id = u.id) " +
            "WHERE t.id = :ticketId AND u.id = :userId")
    boolean hasAccessToTicket(@Param("ticketId") UUID ticketId, @Param("userId") UUID userId);

}
