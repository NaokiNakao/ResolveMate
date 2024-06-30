package com.nakao.resolvemate.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private UUID id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Role role;
}
