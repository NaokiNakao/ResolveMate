package com.nakao.resolvemate.domain.ticket;

import com.nakao.resolvemate.domain.user.Role;
import com.nakao.resolvemate.domain.user.User;
import com.nakao.resolvemate.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TicketService ticketService;

    private User testCustomer;
    private User testSupportAgent;
    private Ticket testTicket;

    @BeforeEach
    public void setUp() {
        testCustomer = User.builder()
                .id(UUID.randomUUID())
                .role(Role.CUSTOMER)
                .build();

        testSupportAgent = User.builder()
                .id(UUID.randomUUID())
                .role(Role.SUPPORT_AGENT)
                .build();

        testTicket = Ticket.builder()
                .id(UUID.randomUUID())
                .customer(testCustomer)
                .build();
    }

    @Test
    public void testCreateTicket() {
        when(userRepository.findAllByRole(Role.SUPPORT_AGENT)).thenReturn(Collections.singletonList(testSupportAgent));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(testTicket);

        TicketDTO ticketDTO = ticketService.createTicket(testTicket);

        assertNotNull(ticketDTO);
        assertEquals(testTicket.getId(), ticketDTO.getId());
        assertEquals(ticketDTO.getSupportAgentId(), testSupportAgent.getId());
        verify(ticketRepository, times(1)).save(testTicket);
    }

}
