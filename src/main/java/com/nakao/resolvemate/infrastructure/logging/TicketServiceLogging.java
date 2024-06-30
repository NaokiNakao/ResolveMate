package com.nakao.resolvemate.infrastructure.logging;

import com.nakao.resolvemate.domain.ticket.TicketDTO;
import com.nakao.resolvemate.domain.util.LogService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class TicketServiceLogging {

    private final LogService<TicketServiceLogging> logService;

    @AfterReturning(pointcut = "execution(* com.nakao.resolvemate.domain.ticket.TicketService.createTicket(..))",
            returning = "result")
    public void createTicketLog(TicketDTO result) {
        logService.info(this, "Ticket created: " + result.getId());
    }

}
