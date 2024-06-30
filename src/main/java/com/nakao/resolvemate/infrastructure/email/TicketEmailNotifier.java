package com.nakao.resolvemate.infrastructure.email;

import com.nakao.resolvemate.domain.ticket.TicketDTO;
import com.nakao.resolvemate.domain.user.User;
import com.nakao.resolvemate.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class TicketEmailNotifier {

    private final EmailService emailService;
    private final UserRepository userRepository;

    @AfterReturning(pointcut = "execution(* com.nakao.resolvemate.domain.ticket.TicketService.createTicket(..))",
            returning = "result")
    public void sendCreatedTicketEmail(TicketDTO result) {
        User supportAgent = userRepository.findById(result.getSupportAgentId()).orElseThrow();
        User customer = userRepository.findById(result.getCustomerId()).orElseThrow();

        notifySupportAgent(supportAgent, result);
        notifyCustomer(customer, result);
    }

    private void notifySupportAgent(User supportAgent, TicketDTO ticket) {
        String subject = "New Ticket Assigned: " + ticket.getTitle();
        String text = String.format(
                """
                Dear %s,
    
                A new ticket has been assigned to you.
    
                Ticket Details:
                Title: %s
                Description: %s
                Status: %s
                Priority: %s
    
                Please take the necessary actions.
    
                Best regards,
                Support Team
                """,
                supportAgent.getFirstName(), ticket.getTitle(), ticket.getDescription(), ticket.getStatus(), ticket.getPriority()
        );
        emailService.sendSimpleMessage(supportAgent.getEmail(), subject, text);
    }

    private void notifyCustomer(User customer, TicketDTO ticket) {
        String subject = "Ticket Created: " + ticket.getTitle();
        String text = String.format(
                """
                Dear %s,
    
                Your ticket has been successfully created.
    
                Ticket Details:
                Title: %s
                Description: %s
                Status: %s
                Priority: %s
    
                We will get back to you shortly.
    
                Best regards,
                Support Team
                """,
                customer.getFirstName(), ticket.getTitle(), ticket.getDescription(), ticket.getStatus(), ticket.getPriority()
        );
        emailService.sendSimpleMessage(customer.getEmail(), subject, text);
    }

}
