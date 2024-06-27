package com.nakao.resolvemate.domain.ticket;

import com.nakao.resolvemate.domain.exception.ResourceNotFoundException;
import com.nakao.resolvemate.domain.exception.UnauthorizedAccessException;
import com.nakao.resolvemate.domain.user.Role;
import com.nakao.resolvemate.domain.user.User;
import com.nakao.resolvemate.domain.user.UserRepository;
import com.nakao.resolvemate.domain.util.AuthorizationService;
import com.nakao.resolvemate.domain.util.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final SecurityService securityService;

    /**
     * Creates a new ticket, assigns it to the authenticated user and a support agent,
     * sets the creation date, and saves it in the repository.
     *
     * @param ticket the ticket to be created
     * @return the created TicketDTO
     */
    public TicketDTO createTicket(Ticket ticket) {
        ticket.setSupportAgent(assignTicketToAgent());
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

        if (AuthorizationService.doesNotHaveAccessToTicket(securityService.getAuthenticatedUser(), ticket)) {
            throw new UnauthorizedAccessException("Unauthorized access");
        }

        return TicketMapper.toDTO(ticket);
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
     * @throws UnauthorizedAccessException if the user does not have the necessary permissions
     */
    private List<Ticket> getTicketsForCurrentUser() {
        User currentUser = securityService.getAuthenticatedUser();
        Role currentUserRole = currentUser.getRole();

        if (currentUserRole == Role.ADMIN) {
            return ticketRepository.findAll();
        } else if (currentUserRole == Role.CUSTOMER) {
            return ticketRepository.findAllByCustomer(currentUser);
        } else if (currentUserRole == Role.SUPPORT_AGENT) {
            return ticketRepository.findAllBySupportAgent(currentUser);
        } else {
            throw new UnauthorizedAccessException("Unauthorized access");
        }
    }

}
