package com.nakao.resolvemate.infrastructure.email;

import com.nakao.resolvemate.domain.comment.CommentDTO;
import com.nakao.resolvemate.domain.ticket.Ticket;
import com.nakao.resolvemate.domain.ticket.TicketRepository;
import com.nakao.resolvemate.domain.user.Role;
import com.nakao.resolvemate.domain.user.User;
import com.nakao.resolvemate.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class CommentEmailNotifier {

    private final EmailService emailService;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;

    @AfterReturning(pointcut = "execution(* com.nakao.resolvemate.domain.comment.CommentService.createComment(..))",
            returning = "result")
    public void sendCreatedCommentEmail(CommentDTO result) {
        User commenter = userRepository.findById(result.getUserId()).orElseThrow();
        Ticket ticket = ticketRepository.findById(result.getTicketId()).orElseThrow();

        if (commenter.getRole().equals(Role.CUSTOMER)) {
            User supportAgent = userRepository.findById(ticket.getSupportAgent().getId()).orElseThrow();
            notifySupportAgent(supportAgent, ticket);
        }
        else if (commenter.getRole().equals(Role.SUPPORT_AGENT)) {
            User customer = userRepository.findById(ticket.getCustomer().getId()).orElseThrow();
            notifyCustomer(customer, ticket);
        }
    }

    private void notifySupportAgent(User supportAgent, Ticket ticket) {
        String subject = "New Comment on Ticket: " + ticket.getTitle();
        String text = String.format(
                """
                Dear %s,
    
                A new comment has been posted on a ticket assigned to you.
    
                Ticket Details:
                Title: %s
                Reference ID: %s
    
                Please review the comment and take any necessary actions.
    
                Best regards,
                Support Team
                """,
                supportAgent.getFirstName(), ticket.getTitle(), ticket.getId()
        );
        emailService.sendSimpleMessage(supportAgent.getEmail(), subject, text);
    }

    private void notifyCustomer(User customer, Ticket ticket) {
        String subject = "New Comment on Your Ticket: " + ticket.getTitle();
        String text = String.format(
                """
                Dear %s,
    
                A new comment has been posted on your ticket.
    
                Ticket Details:
                Title: %s
                Reference ID: %s
    
                We will keep you updated with further information.
    
                Best regards,
                Support Team
                """,
                customer.getFirstName(), ticket.getTitle(), ticket.getId()
        );
        emailService.sendSimpleMessage(customer.getEmail(), subject, text);
    }

}
