package com.nakao.resolvemate.infrastructure.persistance.mapper;

import com.nakao.resolvemate.domain.user.User;
import com.nakao.resolvemate.infrastructure.persistance.entity.UserEntity;

public class UserMapper {

    public static User toModel(UserEntity entity) {
        return User.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .role(entity.getRole())
                .build();
    }

    public static UserEntity toEntity(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .build();
    }

}
