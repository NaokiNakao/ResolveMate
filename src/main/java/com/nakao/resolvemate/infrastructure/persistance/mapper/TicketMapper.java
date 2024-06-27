package com.nakao.resolvemate.infrastructure.persistance.mapper;

import com.nakao.resolvemate.domain.ticket.Ticket;
import com.nakao.resolvemate.infrastructure.persistance.entity.TicketEntity;

public class TicketMapper {

    public static Ticket toModel(TicketEntity entity) {
        return Ticket.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .priority(entity.getPriority())
                .customer(UserMapper.toModel(entity.getCustomer()))
                .supportAgent(UserMapper.toModel(entity.getSupportAgent()))
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public static TicketEntity toEntity(Ticket ticket) {
        return TicketEntity.builder()
                .id(ticket.getId())
                .title(ticket.getTitle())
                .description(ticket.getDescription())
                .status(ticket.getStatus())
                .priority(ticket.getPriority())
                .customer(ticket.getCustomer() != null ? UserMapper.toEntity(ticket.getCustomer()) : null)
                .supportAgent(UserMapper.toEntity(ticket.getSupportAgent()))
                .createdAt(ticket.getCreatedAt())
                .build();
    }

}
