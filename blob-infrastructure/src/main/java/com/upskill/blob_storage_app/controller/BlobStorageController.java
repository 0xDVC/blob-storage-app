package com.upskill.blob_storage_app.controller;

import com.upskill.blob_storage_app.port.input.BlobUseCase;
import com.upskill.blob_storage_app.valueobject.StorageProvider;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/blobs")
@RequiredArgsConstructor
public class BlobStorageController {

    private static final Logger log = LoggerFactory.getLogger(BlobStorageController.class);
    private final BlobUseCase blobUseCase;

    @PostMapping
    public ResponseEntity<String> uploadBlob(
            @RequestHeader("X-API-Key") String apiKey,
            @RequestHeader("X-Storage-Provider") String providerStr,
            @RequestParam("file") MultipartFile file) throws Exception {
        // TODO: Validate API key and get userId
        UUID userId = UUID.randomUUID(); // Temporary
        StorageProvider provider = StorageProvider.valueOf(providerStr.toUpperCase());
        log.info("Uploading blob for user {} with provider {}", userId, provider);
        String blobId = blobUseCase.uploadBlob(userId, file.getBytes(), file.getOriginalFilename(), provider);
        log.info("Blob uploaded: {}", blobId);
        return ResponseEntity.ok(blobId);
    }

    @GetMapping("/{blobId}")
    public ResponseEntity<byte[]> downloadBlob(
            @RequestHeader("X-API-Key") String apiKey,
            @RequestHeader("X-Storage-Provider") String providerStr,
            @PathVariable String blobId) {
        // TODO: Validate API key and get userId
        UUID userId = UUID.randomUUID(); // Temporary
        StorageProvider provider = StorageProvider.valueOf(providerStr.toUpperCase());
        log.info("Downloading blob {} for user {} with provider {}", blobId, userId, provider);
        byte[] content = blobUseCase.downloadBlob(userId, blobId, provider);
        return ResponseEntity.ok(content);
    }

    @DeleteMapping("/{blobId}")
    public ResponseEntity<Void> deleteBlob(
            @RequestHeader("X-API-Key") String apiKey,
            @RequestHeader("X-Storage-Provider") String providerStr,
            @PathVariable String blobId) {
        // TODO: Validate API key and get userId
        UUID userId = UUID.randomUUID(); // Temporary
        StorageProvider provider = StorageProvider.valueOf(providerStr.toUpperCase());
        log.info("Deleting blob {} for user {} with provider {}", blobId, userId, provider);
        blobUseCase.deleteBlob(userId, blobId, provider);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<String>> listBlobs(
            @RequestHeader("X-API-Key") String apiKey,
            @RequestHeader("X-Storage-Provider") String providerStr) {
        // TODO: Validate API key and get userId
        UUID userId = UUID.randomUUID(); // Temporary
        StorageProvider provider = StorageProvider.valueOf(providerStr.toUpperCase());
        log.info("Listing blobs for user {} with provider {}", userId, provider);
        List<String> blobs = blobUseCase.listBlobs(userId, provider);
        return ResponseEntity.ok(blobs);
    }
} 