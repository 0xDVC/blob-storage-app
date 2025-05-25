package com.upskill.blob_storage_app.controller;

import com.upskill.blob_storage_app.entity.User;
import com.upskill.blob_storage_app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BlobStorageControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private String apiKey;

    @BeforeEach
    void setUp() {
        // Create test user and API key
        testUser = User.builder()
                .id(UUID.randomUUID())
                .email("test@example.com")
                .passwordHash("hashedPassword123")
                .build();
        userRepository.save(testUser);
        
        // TODO: Create and save API key for the user
        apiKey = "test-api-key";
    }

    @Test
    void shouldUploadBlob() throws Exception {
        mockMvc.perform(post("/api/v1/blobs")
                .header("X-API-Key", apiKey)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("file", "test.txt"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRejectRequestWithoutApiKey() throws Exception {
        mockMvc.perform(post("/api/v1/blobs")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("file", "test.txt"))
                .andExpect(status().isUnauthorized());
    }
} 