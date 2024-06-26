package com.nakao.resolvemate.infrastructure.persistance.repository.jpa;

import com.nakao.resolvemate.infrastructure.persistance.entity.TicketEntity;
import com.nakao.resolvemate.infrastructure.persistance.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaTicketRepository extends JpaRepository<TicketEntity, UUID> {

    int countBySupportAgent(UserEntity supportAgent);

    List<TicketEntity> findAllByCustomer(UserEntity customer);

    List<TicketEntity> findAllBySupportAgent(UserEntity supportAgent);

}
