package com.nakao.resolvemate.infrastructure.configuration;

import com.nakao.resolvemate.domain.user.Role;
import com.nakao.resolvemate.domain.user.User;
import com.nakao.resolvemate.domain.user.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initializeUsers() {
        initializeAdminUserIfNotFound();
        initializeCustomersIfNotFound();
        initializeSupportAgentsIfNotFound();
    }

    private void initializeAdminUserIfNotFound() {
        if (userRepository.findByEmail("admin@resolvemate.com").isEmpty()) {
            User adminUser = User.builder()
                    .email("admin@resolvemate.com")
                    .password(passwordEncoder.encode("admin"))
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(adminUser);
        }
    }

    private void initializeCustomersIfNotFound() {
        if (userRepository.findByEmail("customer1@example.com").isEmpty()) {
            User customer1 = User.builder()
                    .email("customer1@example.com")
                    .password(passwordEncoder.encode("customer1"))
                    .role(Role.CUSTOMER)
                    .build();
            userRepository.save(customer1);
        }

        if (userRepository.findByEmail("customer2@example.com").isEmpty()) {
            User customer2 = User.builder()
                    .email("customer2@example.com")
                    .password(passwordEncoder.encode("customer2"))
                    .role(Role.CUSTOMER)
                    .build();
            userRepository.save(customer2);
        }

        if (userRepository.findByEmail("customer3@example.com").isEmpty()) {
            User customer3 = User.builder()
                    .email("customer3@example.com")
                    .password(passwordEncoder.encode("customer3"))
                    .role(Role.CUSTOMER)
                    .build();
            userRepository.save(customer3);
        }
    }

    private void initializeSupportAgentsIfNotFound() {
        if (userRepository.findByEmail("agent1@example.com").isEmpty()) {
            User agent1 = User.builder()
                    .email("agent1@example.com")
                    .password(passwordEncoder.encode("agent1"))
                    .role(Role.SUPPORT_AGENT)
                    .build();
            userRepository.save(agent1);
        }

        if (userRepository.findByEmail("agent2@example.com").isEmpty()) {
            User agent2 = User.builder()
                    .email("agent2@example.com")
                    .password(passwordEncoder.encode("agent2"))
                    .role(Role.SUPPORT_AGENT)
                    .build();
            userRepository.save(agent2);
        }

        if (userRepository.findByEmail("agent3@example.com").isEmpty()) {
            User agent3 = User.builder()
                    .email("agent3@example.com")
                    .password(passwordEncoder.encode("agent3"))
                    .role(Role.SUPPORT_AGENT)
                    .build();
            userRepository.save(agent3);
        }
    }

}
