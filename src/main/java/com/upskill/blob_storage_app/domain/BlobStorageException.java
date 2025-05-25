package com.upskill.blob_storage_app.domain;

/**
 * Custom exception for blob storage-related errors.
 */
public class BlobStorageException extends RuntimeException {
    public BlobStorageException(String message) {
        super(message);
    }
    public BlobStorageException(String message, Throwable cause) {
        super(message, cause);
    }
} 