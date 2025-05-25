package com.upskill.blob_storage_app.provider;

import org.springframework.stereotype.Component;

import com.upskill.blob_storage_app.domain.BlobStorageException;
import com.upskill.blob_storage_app.domain.BlobStoragePort;
import com.upskill.blob_storage_app.infrastructure.LocalStorageAdapter;
import com.upskill.blob_storage_app.infrastructure.S3StorageAdapter;

/**
 * Factory for selecting and instantiating the correct storage adapter (strategy) at runtime.
 * <p>
 * Implements the Strategy Factory Pattern for backend selection.
 * Supports extension for new providers (e.g., Azure, GCP).
 */
@Component
public class StorageProviderFactory {
    /**
     * Returns the appropriate BlobStoragePort implementation for the given config.
     * @param config The storage provider configuration.
     * @return The selected BlobStoragePort implementation.
     * @throws IllegalArgumentException if the provider type is unknown.
     */
    public BlobStoragePort getProvider(StorageProviderConfig config) {
        switch (config.getStorageProviderType()) {
            case "local" -> {
                return LocalStorageAdapter.fromConfig(config.getConfig());
            }
            case "s3" -> {
                return S3StorageAdapter.fromConfig(config.getConfig());
            }
            // TODO: Add more cases for Azure and GCP
            default -> {
                throw new BlobStorageException("Unknown storage provider type: " + config.getStorageProviderType());
            }
        }
    }
} 