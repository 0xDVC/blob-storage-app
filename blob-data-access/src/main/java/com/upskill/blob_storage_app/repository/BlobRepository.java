package com.upskill.blob_storage_app.repository;

import com.upskill.blob_storage_app.entity.Blob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BlobRepository extends JpaRepository<Blob, UUID> {
    Optional<Blob> findByBlobId_Value(String blobId);
    Page<Blob> findByUserId(UUID userId, Pageable pageable);
    List<Blob> findByUserId(UUID userId);
    void deleteByBlobId_Value(String blobId);
} 