package com.nakao.resolvemate.domain.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    User save(User user);

    List<User> findAll();

    Optional<User> findById(UUID id);

    void deleteById(UUID id);

    Optional<User> findByEmail(String email);

    List<User> findAllByRole(Role role);

}
