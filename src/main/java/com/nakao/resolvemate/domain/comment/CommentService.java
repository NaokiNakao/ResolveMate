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

    public CommentDTO createComment(UUID ticketId, Comment comment) {
        Ticket ticket = getCurrentTicket(ticketId);
        verifyAuthorization(ticketId);
        comment.setTicket(ticket);
        CommentDTO createdComment = CommentMapper.toDTO(commentRepository.save(comment));
        logService.info(this, "Comment created: " + createdComment.getId());
        return createdComment;
    }

    public List<CommentDTO> getCommentsByTicketId(UUID ticketId) {
        Ticket ticket = getCurrentTicket(ticketId);

        verifyAuthorization(ticketId);

        List<CommentDTO> comments = commentRepository.findAllByTicket(ticket).stream()
                .map(CommentMapper::toDTO)
                .toList();

        logService.info(this, "Found " + comments.size() + " comments for ticket " + ticketId);

        return comments;
    }

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
