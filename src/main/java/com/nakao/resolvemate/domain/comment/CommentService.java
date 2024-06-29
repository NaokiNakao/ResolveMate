package com.nakao.resolvemate.domain.comment;

import com.nakao.resolvemate.domain.exception.ResourceNotFoundException;
import com.nakao.resolvemate.domain.exception.ForbiddenAccessException;
import com.nakao.resolvemate.domain.ticket.Ticket;
import com.nakao.resolvemate.domain.ticket.TicketRepository;
import com.nakao.resolvemate.domain.user.Role;
import com.nakao.resolvemate.domain.user.User;
import com.nakao.resolvemate.domain.util.LogService;
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
    private final LogService<CommentService> logService;

    /**
     * Creates a new comment for a specific ticket.
     *
     * @param ticketId the UUID of the ticket to comment on
     * @param comment the comment to create
     * @return the created comment as a CommentDTO
     * @throws ResourceNotFoundException if the ticket is not found
     */
    public CommentDTO createComment(UUID ticketId, Comment comment) {
        Ticket ticket = getCurrentTicket(ticketId);
        verifyAuthorization(ticketId);
        comment.setTicket(ticket);
        CommentDTO createdComment = CommentMapper.toDTO(commentRepository.save(comment));
        logService.info(this, "Comment created: " + createdComment.getId());
        return createdComment;
    }

    /**
     * Retrieves all comments for a specific ticket, ensuring the current user has access to the ticket.
     *
     * @param ticketId the UUID of the ticket
     * @return a list of CommentDTOs for the comments associated with the ticket
     * @throws ResourceNotFoundException if the ticket is not found
     */
    public List<CommentDTO> getCommentsByTicketId(UUID ticketId) {
        Ticket ticket = getCurrentTicket(ticketId);

        verifyAuthorization(ticketId);

        List<CommentDTO> comments = commentRepository.findAllByTicket(ticket).stream()
                .map(CommentMapper::toDTO)
                .toList();

        logService.info(this, "Found " + comments.size() + " comments for ticket " + ticketId);

        return comments;
    }

    /**
     * Verifies if the authenticated user has authorization to access the specified ticket.
     *
     * @param ticketId the ID of the ticket to check authorization against
     * @throws ForbiddenAccessException if the user does not have access to the ticket
     */
    private void verifyAuthorization(UUID ticketId) {
        User currentUser = securityService.getAuthenticatedUser();

        if (!ticketRepository.hasAccessToTicket(ticketId, currentUser.getId()) &&
                !Objects.equals(currentUser.getRole(), Role.ADMIN)) {
            String message = "Unauthorized access for ticket " + ticketId + " by user " + currentUser.getId();
            logService.warn(this, message);
            throw new ForbiddenAccessException(message);
        }
    }

    private Ticket getCurrentTicket(UUID ticketId) {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> {
                    String message = "Ticket not found with ID: " + ticketId;
                    logService.warn(this, message);
                    return new ResourceNotFoundException(message);
                });
    }

}
