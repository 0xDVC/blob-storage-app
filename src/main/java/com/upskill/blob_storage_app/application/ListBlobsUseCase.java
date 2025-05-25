package com.upskill.blob_storage_app.application;

import java.util.List;

import com.upskill.blob_storage_app.domain.BlobStoragePort;

/**
 * Use case for listing blobs in a directory.
 * <p>
 * Orchestrates the list operation via the {@link BlobStoragePort}.
 */
public class ListBlobsUseCase {
    private final BlobStoragePort storagePort;

    /**
     * Constructs the use case with the required storage port.
     * @param storagePort The port to use for storage operations.
     */
    public ListBlobsUseCase(BlobStoragePort storagePort) {
        this.storagePort = storagePort;
    }

    /**
     * Executes the list operation.
     * @param directory The directory to list blobs from.
     * @return List of blob names in the directory.
     */
    public List<String> execute(String directory) {
        return storagePort.list(directory);
    }
}