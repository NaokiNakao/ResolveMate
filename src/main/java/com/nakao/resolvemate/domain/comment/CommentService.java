package com.nakao.resolvemate.domain.comment;

import com.nakao.resolvemate.domain.exception.ResourceNotFoundException;
import com.nakao.resolvemate.domain.exception.UnauthorizedAccessException;
import com.nakao.resolvemate.domain.ticket.Ticket;
import com.nakao.resolvemate.domain.ticket.TicketRepository;
import com.nakao.resolvemate.domain.user.User;
import com.nakao.resolvemate.domain.util.AAAService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final TicketRepository ticketRepository;
    private final CommentRepository commentRepository;

    /**
     * Creates a new comment for a specific ticket.
     *
     * @param ticketId the UUID of the ticket to comment on
     * @param comment the comment to create
     * @return the created comment as a CommentDTO
     * @throws ResourceNotFoundException if the ticket is not found
     * @throws UnauthorizedAccessException if the current user does not have access to the ticket
     */
    public CommentDTO createComment(UUID ticketId, Comment comment) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + ticketId));

        if (AAAService.doesNotHaveAccessToTicket(ticket)) {
            throw new UnauthorizedAccessException("Unauthorized access");
        }

        User currentUser = AAAService.getAuthenticatedUser();

        comment.setTicket(ticket);
        comment.setUser(currentUser);
        comment.setCreatedAt(new Date());
        return CommentMapper.toDTO(commentRepository.save(comment));
    }

    /**
     * Retrieves all comments for a specific ticket, ensuring the current user has access to the ticket.
     *
     * @param ticketId the UUID of the ticket
     * @return a list of CommentDTOs for the comments associated with the ticket
     * @throws ResourceNotFoundException if the ticket is not found
     * @throws UnauthorizedAccessException if the current user does not have access to the ticket
     */
    public List<CommentDTO> getCommentsByTicketId(UUID ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + ticketId));

        if (AAAService.doesNotHaveAccessToTicket(ticket)) {
            throw new UnauthorizedAccessException("Unauthorized access");
        }

        return commentRepository.findAllByTicket(ticket).stream()
                .map(CommentMapper::toDTO)
                .collect(Collectors.toList());
    }

}
