package com.upskill.blob_storage_app.application;

import com.upskill.blob_storage_app.domain.BlobStoragePort;

/**
 * Use case for saving a blob to storage.
 * <p>
 * Orchestrates the save operation via the {@link BlobStoragePort}.
 */
public class SaveBlobUseCase {
    /** The port through which storage operations are performed. */
    private final BlobStoragePort storagePort;

    /**
     * Constructs the use case with the required storage port.
     * @param storagePort The port to use for storage operations.
     */
    public SaveBlobUseCase(BlobStoragePort storagePort) {
        this.storagePort = storagePort;
    }

    /**
     * Executes the save operation.
     * @param path The path to save the blob at.
     * @param content The blob content as a byte array.
     */
    public void execute(String path, byte[] content) {
        storagePort.save(path, content);
    }
} 