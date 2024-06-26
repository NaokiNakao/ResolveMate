package com.nakao.resolvemate.domain.util;

import com.nakao.resolvemate.domain.ticket.Ticket;
import com.nakao.resolvemate.domain.user.Role;
import com.nakao.resolvemate.domain.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public class AAAService {

    /**
     * Retrieves the authenticated user from the security context.
     *
     * @return the authenticated User
     */
    public static User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    /**
     * Checks if the authenticated user does not have access to a specific ticket.
     *
     * @param ticket the ticket to check access for
     * @return true if the user does not have access, false otherwise
     */
    public static boolean doesNotHaveAccessToTicket(Ticket ticket) {
        User user = getAuthenticatedUser();

        Role userRole = user.getRole();
        return userRole != Role.ADMIN &&
                (userRole != Role.CUSTOMER || !Objects.equals(ticket.getCustomer(), user)) &&
                (userRole != Role.SUPPORT_AGENT || !Objects.equals(ticket.getSupportAgent(), user));
    }

}
