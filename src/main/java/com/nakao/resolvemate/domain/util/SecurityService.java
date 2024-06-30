package com.nakao.resolvemate.domain.util;

import com.nakao.resolvemate.domain.user.User;

public interface SecurityService {

    User getAuthenticatedUser();

}
