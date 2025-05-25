package com.upskill.blob_storage_app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for version 1 of the Blob Storage API.
 *
 * <p>This controller supports two modes:
 * 1. Bring Your Own Credentials (BYOC): Users generate pre-signed URLs themselves and notify the API.
 * 2. Managed Credentials: Users register their provider configs (securely encrypted), and the API generates pre-signed URLs on their behalf.
 *
 * All sensitive data is encrypted at rest and never logged. Users can choose their preferred mode.
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/blobs")
public class BlobStorageControllerV1 {
    // --- BYOC Mode: Stateless Notification Endpoints ---

    /**
     * Notify the system of a completed upload via pre-signed URL (BYOC mode).
     * No file data or credentials are handled here.
     */
    @Operation(summary = "Notify of a completed upload via pre-signed URL (BYOC mode)")
    @PostMapping("/notify-upload")
    public ResponseEntity<Void> notifyUpload(
            @RequestHeader("Authorization") String apiKey,
            @RequestParam String provider,
            @RequestParam String path,
            @RequestParam String presignedUrl,
            @RequestParam(required = false) String metadata) {
        // Privacy-conscious logging: do NOT log presignedUrl or sensitive data
        log.info("[v1][BYOC] Upload notification: provider={}, path={}, metadata={}", provider, path, metadata);
        // Optionally: store metadata, trigger workflows, etc.
        return ResponseEntity.ok().build();
    }

    /**
     * Notify the system of a completed download via pre-signed URL (BYOC mode).
     */
    @Operation(summary = "Notify of a completed download via pre-signed URL (BYOC mode)")
    @PostMapping("/notify-download")
    public ResponseEntity<Void> notifyDownload(
            @RequestHeader("Authorization") String apiKey,
            @RequestParam String provider,
            @RequestParam String path,
            @RequestParam String presignedUrl) {
        log.info("[v1][BYOC] Download notification: provider={}, path={}", provider, path);
        return ResponseEntity.ok().build();
    }

    /**
     * Notify the system of a completed delete via pre-signed URL (BYOC mode).
     */
    @Operation(summary = "Notify of a completed delete via pre-signed URL (BYOC mode)")
    @PostMapping("/notify-delete")
    public ResponseEntity<Void> notifyDelete(
            @RequestHeader("Authorization") String apiKey,
            @RequestParam String provider,
            @RequestParam String path,
            @RequestParam String presignedUrl) {
        log.info("[v1][BYOC] Delete notification: provider={}, path={}", provider, path);
        return ResponseEntity.ok().build();
    }

    // --- Managed Credentials Mode: Provider Config Management & Pre-signed URL Generation ---

    /**
     * Register or update a provider config (Managed Credentials mode).
     * All sensitive fields must be encrypted at rest.
     */
    @Operation(summary = "Register or update a provider config (Managed Credentials mode)")
    @PostMapping("/provider-config")
    public ResponseEntity<Void> registerOrUpdateProviderConfig(
            @RequestHeader("Authorization") String apiKey,
            @RequestParam String providerId,
            @RequestParam String providerType,
            @RequestParam String configJson // Should include credentials, encrypted at rest
    ) {
        // TODO: encrypt and store the config securely, with the associated user
        log.info("[v1][Managed] Provider config registered/updated: providerId={}, providerType={}", providerId, providerType);
        return ResponseEntity.ok().build();
    }

    /**
     * Delete a provider config (Managed Credentials mode).
     */
    @Operation(summary = "Delete a provider config (Managed Credentials mode)")
    @DeleteMapping("/provider-config")
    public ResponseEntity<Void> deleteProviderConfig(
            @RequestHeader("Authorization") String apiKey,
            @RequestParam String providerId
    ) {
        // TODO: delete the provider config for this user
        log.info("[v1][Managed] Provider config deleted: providerId={}", providerId);
        return ResponseEntity.ok().build();
    }

    /**
     * Generate a pre-signed URL for upload/download/delete (Managed Credentials mode).
     * The API uses the securely stored provider config to generate the URL.
     */
    @Operation(summary = "Generate a pre-signed URL for a blob operation (Managed Credentials mode)")
    @PostMapping("/presign")
    public ResponseEntity<String> generatePresignedUrl(
            @RequestHeader("Authorization") String apiKey,
            @RequestParam String providerId,
            @RequestParam String operation, // upload, download, delete
            @RequestParam String path,
            @RequestParam(required = false) String metadata
    ) {
        // TODO: fetch and decrypt provider config, generate pre-signed URL for the operation
        log.info("[v1][Managed] Pre-signed URL requested: providerId={}, operation={}, path={}", providerId, operation, path);
        String presignedUrl = "<generated-url>"; // Placeholder
        return ResponseEntity.ok(presignedUrl);
    }
} 