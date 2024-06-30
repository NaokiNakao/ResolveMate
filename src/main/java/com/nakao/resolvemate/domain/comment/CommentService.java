package com.nakao.resolvemate.domain.comment;

import com.nakao.resolvemate.domain.exception.ResourceNotFoundException;
import com.nakao.resolvemate.domain.exception.ForbiddenAccessException;
import com.nakao.resolvemate.domain.ticket.Ticket;
import com.nakao.resolvemate.domain.ticket.TicketRepository;
import com.nakao.resolvemate.domain.user.Role;
import com.nakao.resolvemate.domain.user.User;
import com.nakao.resolvemate.domain.util.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final TicketRepository ticketRepository;
    private final CommentRepository commentRepository;
    private final SecurityService securityService;

    public CommentDTO createComment(UUID ticketId, Comment comment) {
        Ticket ticket = getCurrentTicket(ticketId);
        verifyAuthorization(ticketId);
        comment.setTicket(ticket);
        return CommentMapper.toDTO(commentRepository.save(comment));
    }

    public List<CommentDTO> getCommentsByTicketId(UUID ticketId) {
        Ticket ticket = getCurrentTicket(ticketId);

        verifyAuthorization(ticketId);

        return commentRepository.findAllByTicket(ticket).stream()
                .map(CommentMapper::toDTO)
                .toList();
    }

    private void verifyAuthorization(UUID ticketId) {
        User currentUser = securityService.getAuthenticatedUser();

        if (!ticketRepository.hasAccessToTicket(ticketId, currentUser.getId()) &&
                !Objects.equals(currentUser.getRole(), Role.ADMIN)) {
            throw new ForbiddenAccessException("Unauthorized access for ticket " + ticketId + " by user " + currentUser.getId());
        }
    }

    private Ticket getCurrentTicket(UUID ticketId) {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with ID: " + ticketId));
    }

}
