package com.nakao.resolvemate.domain.ticket;

public class TicketMapper {

    public static TicketDTO toDTO(Ticket ticket) {
        return TicketDTO.builder()
                .id(ticket.getId())
                .title(ticket.getTitle())
                .description(ticket.getDescription())
                .status(ticket.getStatus())
                .priority(ticket.getPriority())
                .customerId(ticket.getCustomer().getId())
                .supportAgentId(ticket.getSupportAgent().getId())
                .createdAt(ticket.getCreatedAt())
                .build();
    }

}
