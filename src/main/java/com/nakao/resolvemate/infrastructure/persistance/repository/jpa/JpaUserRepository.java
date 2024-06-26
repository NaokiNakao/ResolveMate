package com.nakao.resolvemate.infrastructure.persistance.repository.jpa;

import com.nakao.resolvemate.domain.user.Role;
import com.nakao.resolvemate.infrastructure.persistance.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaUserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByEmail(String email);

    List<UserEntity> findAllByRole(Role role);

}
