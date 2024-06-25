package com.nakao.resolvemate.infrastructure.persistance.repository;

import com.nakao.resolvemate.domain.ticket.Ticket;
import com.nakao.resolvemate.domain.ticket.TicketRepository;
import com.nakao.resolvemate.domain.user.User;
import com.nakao.resolvemate.infrastructure.persistance.entity.TicketEntity;
import com.nakao.resolvemate.infrastructure.persistance.mapper.TicketMapper;
import com.nakao.resolvemate.infrastructure.persistance.mapper.UserMapper;
import com.nakao.resolvemate.infrastructure.persistance.repository.jpa.JpaTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TicketRepositoryImpl implements TicketRepository {

    private final JpaTicketRepository repository;

    @Override
    public Ticket save(Ticket ticket) {
        TicketEntity ticketEntity = TicketMapper.toEntity(ticket);
        return TicketMapper.toModel(repository.save(ticketEntity));
    }

    @Override
    public List<Ticket> findAll() {
        return repository.findAll().stream()
                .map(TicketMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Ticket> findById(UUID id) {
        return repository.findById(id).map(TicketMapper::toModel);
    }

    @Override
    public int countBySupportAgent(User supportAgent) {
        return repository.countBySupportAgent(UserMapper.toEntity(supportAgent));
    }

    @Override
    public List<Ticket> findByCustomer(User customer) {
        return repository.findByCustomer(UserMapper.toEntity(customer)).stream()
                .map(TicketMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Ticket> findBySupportAgent(User supportAgent) {
        return repository.findBySupportAgent(UserMapper.toEntity(supportAgent)).stream()
                .map(TicketMapper::toModel)
                .collect(Collectors.toList());
    }

}
