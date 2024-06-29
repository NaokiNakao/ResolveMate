package com.nakao.resolvemate.domain.ticket;

import com.nakao.resolvemate.domain.exception.ResourceNotFoundException;
import com.nakao.resolvemate.domain.exception.ForbiddenAccessException;
import com.nakao.resolvemate.domain.user.Role;
import com.nakao.resolvemate.domain.user.User;
import com.nakao.resolvemate.domain.user.UserRepository;
import com.nakao.resolvemate.domain.util.LogService;
import com.nakao.resolvemate.domain.util.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final SecurityService securityService;
    private final LogService<TicketService> logService;

    /**
     * Creates a new ticket, assigns it to the authenticated user and a support agent
     *
     * @param ticket the ticket to be created
     * @return the created TicketDTO
     */
    public TicketDTO createTicket(Ticket ticket) {
        ticket.setSupportAgent(assignTicketToAgent());
        Ticket createdTicket = ticketRepository.save(ticket);
        logService.info(this, "Ticket created: " + createdTicket.getId());
        return TicketMapper.toDTO(createdTicket);
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
     * @throws ForbiddenAccessException if the user does not have access to the ticket
     */
    public TicketDTO getTicketById(UUID id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> {
                    String message = "Ticket not found with id: " + id;
                    logService.warn(this, message);
                    return new ResourceNotFoundException(message);
                });

        verifyAuthorization(id);
        logService.info(this, "Ticket retrieved successfully: " + id);
        return TicketMapper.toDTO(ticket);
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
            String message = "Unauthorized access for " + ticketId + " by user " + currentUser.getId();
            logService.warn(this, message);
            throw new ForbiddenAccessException(message);
        }
    }

    /**
     * Assigns a ticket to a support agent with the least number of tickets assigned.
     *
     * @return the User assigned as the support agent
     * @throws ResourceNotFoundException if no support agents are found
     */
    private User assignTicketToAgent() {
        List<User> agents = userRepository.findAllByRole(Role.SUPPORT_AGENT);
        return agents.stream()
                .min((agent1, agent2) -> Integer.compare(ticketRepository.countBySupportAgent(agent1), ticketRepository.countBySupportAgent(agent2)))
                .orElseThrow(() -> new ResourceNotFoundException("No support agents found"));
    }

    /**
     * Retrieves the list of tickets accessible to the authenticated user based on their role.
     *
     * @return a list of accessible tickets
     * @throws ForbiddenAccessException if the user does not have the necessary permissions
     */
    private List<Ticket> getTicketsForCurrentUser() {
        User currentUser = securityService.getAuthenticatedUser();
        Role currentUserRole = currentUser.getRole();
        List<Ticket> tickets;

        if (currentUserRole == Role.ADMIN) {
            tickets = ticketRepository.findAll();
        } else if (currentUserRole == Role.CUSTOMER) {
            tickets = ticketRepository.findAllByCustomer(currentUser);
        } else if (currentUserRole == Role.SUPPORT_AGENT) {
            tickets = ticketRepository.findAllBySupportAgent(currentUser);
        } else {
            throw new ForbiddenAccessException("Unauthorized access");
        }

        logService.info(this, "Found " + tickets.size() + " tickets for user " + currentUser.getId());

        return tickets;
    }

}
