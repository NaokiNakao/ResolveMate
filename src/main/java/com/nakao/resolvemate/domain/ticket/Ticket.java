package com.nakao.resolvemate.domain.ticket;

import com.nakao.resolvemate.domain.user.User;
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
public class Ticket {
    private UUID id;
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private User customer;
    private User supportAgent;
    private Date createdAt;
}
