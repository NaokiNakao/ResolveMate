package com.nakao.resolvemate.domain.util;

import com.nakao.resolvemate.domain.user.User;

public interface SecurityService {

    /**
     * Retrieves the currently authenticated user.
     *
     * @return the authenticated User object
     */
    User getAuthenticatedUser();

}
