package com.nakao.resolvemate.domain.ticket;

import com.nakao.resolvemate.domain.comment.Comment;
import com.nakao.resolvemate.domain.comment.CommentDTO;
import com.nakao.resolvemate.domain.comment.CommentMapper;
import com.nakao.resolvemate.domain.comment.CommentRepository;
import com.nakao.resolvemate.domain.exception.ResourceNotFoundException;
import com.nakao.resolvemate.domain.exception.UnauthorizedAccessException;
import com.nakao.resolvemate.domain.user.Role;
import com.nakao.resolvemate.domain.user.User;
import com.nakao.resolvemate.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    /**
     * Creates a new ticket, assigns it to the authenticated user and a support agent,
     * sets the creation date, and saves it in the repository.
     *
     * @param ticket the ticket to be created
     * @return the created TicketDTO
     */
    public TicketDTO createTicket(Ticket ticket) {
        ticket.setCustomer(getAuthenticatedUser());
        ticket.setSupportAgent(assignTicketToAgent());
        ticket.setCreatedAt(new Date());
        return TicketMapper.toDTO(ticketRepository.save(ticket));
    }

    /**
     * Retrieves all tickets accessible to the authenticated user.
     *
     * @return a list of TicketDTOs
     */
    public List<TicketDTO> getAllTickets() {
        List<Ticket> tickets = getTicketsForCurrentUser();
        return tickets.stream()
                .map(TicketMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a ticket by its ID if the authenticated user has access to it.
     *
     * @param id the UUID of the ticket
     * @return the TicketDTO
     * @throws ResourceNotFoundException if the ticket is not found
     * @throws UnauthorizedAccessException if the user does not have access to the ticket
     */
    public TicketDTO getTicketById(UUID id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + id));

        User currentUser = getAuthenticatedUser();

        if (hasAccessToTicket(currentUser, ticket)) {
            return TicketMapper.toDTO(ticket);
        }

        throw new UnauthorizedAccessException("Unauthorized access");
    }

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

        User currentUser = getAuthenticatedUser();

        if (hasAccessToTicket(currentUser, ticket)) {
            comment.setTicket(ticket);
            comment.setUser(currentUser);
            comment.setCreatedAt(new Date());
            return CommentMapper.toDTO(commentRepository.save(comment));
        }

        throw new UnauthorizedAccessException("Unauthorized access");
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

        User currentUser = getAuthenticatedUser();

        if (hasAccessToTicket(currentUser, ticket)) {
            return commentRepository.findByTicket(ticket).stream()
                    .map(CommentMapper::toDTO)
                    .collect(Collectors.toList());
        }

        throw new UnauthorizedAccessException("Unauthorized access");
    }

    /**
     * Retrieves the authenticated user from the security context.
     *
     * @return the authenticated User
     */
    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    /**
     * Assigns a ticket to a support agent with the least number of tickets assigned.
     *
     * @return the User assigned as the support agent
     * @throws ResourceNotFoundException if no support agents are found
     */
    private User assignTicketToAgent() {
        List<User> agents = userRepository.findByRole(Role.SUPPORT_AGENT);
        return agents.stream()
                .min((agent1, agent2) -> Integer.compare(ticketRepository.countBySupportAgent(agent1), ticketRepository.countBySupportAgent(agent2)))
                .orElseThrow(() -> new ResourceNotFoundException("No support agents found"));
    }

    /**
     * Retrieves the list of tickets accessible to the authenticated user based on their role.
     *
     * @return a list of accessible tickets
     * @throws UnauthorizedAccessException if the user does not have the necessary permissions
     */
    private List<Ticket> getTicketsForCurrentUser() {
        User currentUser = getAuthenticatedUser();
        Role currentUserRole = currentUser.getRole();

        if (currentUserRole == Role.ADMIN) {
            return ticketRepository.findAll();
        } else if (currentUserRole == Role.CUSTOMER) {
            return ticketRepository.findByCustomer(currentUser);
        } else if (currentUserRole == Role.SUPPORT_AGENT) {
            return ticketRepository.findBySupportAgent(currentUser);
        } else {
            throw new UnauthorizedAccessException("Unauthorized access");
        }
    }

    /**
     * Checks if the authenticated user has access to a specific ticket.
     *
     * @param user the authenticated user
     * @param ticket the ticket to check access for
     * @return true if the user has access, false otherwise
     */
    private boolean hasAccessToTicket(User user, Ticket ticket) {
        Role userRole = user.getRole();
        return userRole == Role.ADMIN ||
                (userRole == Role.CUSTOMER && Objects.equals(ticket.getCustomer(), user)) ||
                (userRole == Role.SUPPORT_AGENT && Objects.equals(ticket.getSupportAgent(), user));
    }

}
