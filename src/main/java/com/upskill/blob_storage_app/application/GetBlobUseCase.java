package com.upskill.blob_storage_app.application;

import com.upskill.blob_storage_app.domain.BlobStoragePort;

/**
 * Use case for retrieving a blob from storage.
 * <p>
 * Orchestrates the get operation via the {@link BlobStoragePort}.
 */
public class GetBlobUseCase {
    private final BlobStoragePort storagePort;

    /**
     * Constructs the use case with the required storage port.
     * @param storagePort The port to use for storage operations.
     */
    public GetBlobUseCase(BlobStoragePort storagePort) {
        this.storagePort = storagePort;
    }

    /**
     * 
     * Executes the get operation.
     * @param path The path of the blob to retrieve.
     * @return The blob content as a byte array.
     */
    public byte[] execute(String path) {
        return storagePort.get(path);
    }
} 