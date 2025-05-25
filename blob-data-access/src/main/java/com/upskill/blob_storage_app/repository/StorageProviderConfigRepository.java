package com.upskill.blob_storage_app.repository;

import com.upskill.blob_storage_app.entity.StorageProviderConfig;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageProviderConfigRepository extends JpaRepository<StorageProviderConfig, UUID> {
    List<StorageProviderConfig> findByUserId(UUID userId);
    List<StorageProviderConfig> findByUserIdAndIsActiveTrue(UUID userId);
} 