package com.nakao.resolvemate.domain.ticket;

import com.nakao.resolvemate.domain.exception.ResourceNotFoundException;
import com.nakao.resolvemate.domain.exception.ForbiddenAccessException;
import com.nakao.resolvemate.domain.user.Role;
import com.nakao.resolvemate.domain.user.User;
import com.nakao.resolvemate.domain.user.UserRepository;
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

    public TicketDTO createTicket(Ticket ticket) {
        ticket.setSupportAgent(assignTicketToAgent());
        Ticket createdTicket = ticketRepository.save(ticket);
        return TicketMapper.toDTO(createdTicket);
    }

    public List<TicketDTO> getAllTickets() {
        List<Ticket> tickets = getTicketsForCurrentUser();
        return tickets.stream()
                .map(TicketMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TicketDTO getTicketById(UUID id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + id));

        verifyAuthorization(id);
        return TicketMapper.toDTO(ticket);
    }

    private void verifyAuthorization(UUID ticketId) {
        User currentUser = securityService.getAuthenticatedUser();

        if (!ticketRepository.hasAccessToTicket(ticketId, currentUser.getId()) &&
                !Objects.equals(currentUser.getRole(), Role.ADMIN)) {
            throw new ForbiddenAccessException("Unauthorized access for " + ticketId + " by user " + currentUser.getId());
        }
    }

    private User assignTicketToAgent() {
        List<User> agents = userRepository.findAllByRole(Role.SUPPORT_AGENT);
        return agents.stream()
                .min((agent1, agent2) -> Integer.compare(ticketRepository.countBySupportAgent(agent1), ticketRepository.countBySupportAgent(agent2)))
                .orElseThrow(() -> new ResourceNotFoundException("No support agents found"));
    }

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

        return tickets;
    }

}
