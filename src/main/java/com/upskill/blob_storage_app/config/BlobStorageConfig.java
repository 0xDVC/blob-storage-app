package com.upskill.blob_storage_app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.upskill.blob_storage_app.application.DeleteBlobUseCase;
import com.upskill.blob_storage_app.application.GetBlobUseCase;
import com.upskill.blob_storage_app.application.ListBlobsUseCase;
import com.upskill.blob_storage_app.application.SaveBlobUseCase;
import com.upskill.blob_storage_app.domain.BlobStoragePort;
import com.upskill.blob_storage_app.infrastructure.LocalStorageAdapter;

/**
 * Spring configuration for blob storage wiring.
 *
 * <p>Defines beans for the storage adapter and all use case classes.
 * You can easily swap the adapter here (e.g., to S3) as needed.
 */
@Configuration
public class BlobStorageConfig {

    /**
     * Defines the BlobStoragePort bean, using the local file system by default.
     * The root directory is configurable via application.properties.
     * @param rootDir The root directory for blob storage.
     * @return The BlobStoragePort implementation.
     */
    @Bean
    public BlobStoragePort blobStoragePort(@Value("${blob.storage.root:blobs}") String rootDir) {
        return new LocalStorageAdapter(rootDir);
    }

    @Bean
    public SaveBlobUseCase saveBlobUseCase(BlobStoragePort port) {
        return new SaveBlobUseCase(port);
    }

    @Bean
    public GetBlobUseCase getBlobUseCase(BlobStoragePort port) {
        return new GetBlobUseCase(port);
    }

    @Bean
    public ListBlobsUseCase listBlobsUseCase(BlobStoragePort port) {
        return new ListBlobsUseCase(port);
    }

    @Bean
    public DeleteBlobUseCase deleteBlobUseCase(BlobStoragePort port) {
        return new DeleteBlobUseCase(port);
    }
} 