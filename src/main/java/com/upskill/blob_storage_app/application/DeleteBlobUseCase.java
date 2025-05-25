package com.upskill.blob_storage_app.application;

import com.upskill.blob_storage_app.domain.BlobStoragePort;

/**
 * Use case for deleting a blob from storage.
 * <p>
 * Orchestrates the delete operation via the {@link BlobStoragePort}.
 */
public class DeleteBlobUseCase {
    private final BlobStoragePort storagePort;

    /**
     * Constructs the use case with the required storage port.
     * @param storagePort The port to use for storage operations.
     */
    public DeleteBlobUseCase(BlobStoragePort storagePort) {
        this.storagePort = storagePort;
    }

    /**
     * Executes the delete operation.
     * @param path The path of the blob to delete.
     */
    public void execute(String path) {
        storagePort.delete(path);
    }
} 