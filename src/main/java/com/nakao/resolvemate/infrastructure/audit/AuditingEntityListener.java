package com.nakao.resolvemate.infrastructure.audit;

import com.nakao.resolvemate.infrastructure.persistance.entity.UserEntity;
import jakarta.persistence.PrePersist;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;

public class AuditingEntityListener {

    @PrePersist
    public void create(Auditable auditable) {
        auditable.setCreatedDate(new Date());
        auditable.setCreatedBy(getAuthenticatedUser());
    }

    private UserEntity getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserEntity) authentication.getPrincipal();
    }

}
