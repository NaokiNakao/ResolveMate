package com.nakao.resolvemate.domain.ticket;

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
public class TicketDTO {
    private UUID id;
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private UUID customerId;
    private UUID supportAgentId;
    private Date createdAt;
}
