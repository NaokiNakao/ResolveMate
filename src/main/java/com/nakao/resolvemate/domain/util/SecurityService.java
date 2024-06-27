package com.nakao.resolvemate.domain.util;

import com.nakao.resolvemate.domain.user.User;

public interface SecurityService {

    /**
     * Retrieves the authenticated user from the security context.
     *
     * @return the authenticated User
     */
    User getAuthenticatedUser();

}
