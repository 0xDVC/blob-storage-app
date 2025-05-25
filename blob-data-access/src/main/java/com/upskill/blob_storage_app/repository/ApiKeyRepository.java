package com.upskill.blob_storage_app.repository;

import com.upskill.blob_storage_app.entity.ApiKey;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, UUID> {
    List<ApiKey> findByUserId(UUID userId);
    Optional<ApiKey> findByKeyHash(String keyHash);
    boolean existsByKeyHash(String keyHash);
} 