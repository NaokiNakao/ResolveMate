package com.nakao.resolvemate.infrastructure.security;

import com.nakao.resolvemate.domain.user.User;
import com.nakao.resolvemate.domain.util.SecurityService;
import com.nakao.resolvemate.infrastructure.persistance.entity.UserEntity;
import com.nakao.resolvemate.infrastructure.persistance.mapper.UserMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();
        return UserMapper.toModel(user);
    }

}
