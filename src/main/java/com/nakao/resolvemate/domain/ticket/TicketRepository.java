package com.nakao.resolvemate.domain.ticket;

import com.nakao.resolvemate.domain.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TicketRepository {

    Ticket save(Ticket ticket);

    List<Ticket> findAll();

    Optional<Ticket> findById(UUID id);

    int countBySupportAgent(User supportAgent);

    List<Ticket> findAllByCustomer(User customer);

    List<Ticket> findAllBySupportAgent(User supportAgent);

}
