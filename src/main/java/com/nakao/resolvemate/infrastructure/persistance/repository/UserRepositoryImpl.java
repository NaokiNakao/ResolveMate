package com.nakao.resolvemate.infrastructure.persistance.repository;

import com.nakao.resolvemate.domain.user.Role;
import com.nakao.resolvemate.domain.user.User;
import com.nakao.resolvemate.domain.user.UserRepository;
import com.nakao.resolvemate.infrastructure.persistance.entity.UserEntity;
import com.nakao.resolvemate.infrastructure.persistance.repository.jpa.JpaUserRepository;
import com.nakao.resolvemate.infrastructure.persistance.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository repository;

    @Override
    public User save(User user) {
        UserEntity userEntity = UserMapper.toEntity(user);
        return UserMapper.toModel(repository.save(userEntity));
    }

    @Override
    public List<User> findAll() {
        return repository.findAll().stream().map(UserMapper::toModel).collect(Collectors.toList());
    }

    @Override
    public Optional<User> findById(UUID id) {
        return repository.findById(id).map(UserMapper::toModel);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email).map(UserMapper::toModel);
    }

    @Override
    public List<User> findByRole(Role role) {
        return repository.findByRole(role).stream().map(UserMapper::toModel).collect(Collectors.toList());
    }

}
