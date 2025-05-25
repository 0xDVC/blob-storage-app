package com.upskill.blob_storage_app.entity;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

class UserTest {
    
    @Test
    void shouldCreateUserWithBuilder() {
        UUID id = UUID.randomUUID();
        String email = "test@example.com";
        String passwordHash = "hashedPassword123";

        User user = User.builder()
                .id(id)
                .email(email)
                .passwordHash(passwordHash)
                .build();

        assertThat(user.getId()).isEqualTo(id);
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getPasswordHash()).isEqualTo(passwordHash);
    }

    @Test
    void shouldCreateUserWithNoArgsConstructor() {
        User user = new User();
        assertThat(user).isNotNull();
    }
} 