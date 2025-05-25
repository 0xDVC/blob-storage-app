package com.upskill.blob_storage_app.exception;

public class BlobStorageException extends RuntimeException {
    public BlobStorageException(String message) {
        super(message);
    }
    public BlobStorageException(String message, Throwable cause) {
        super(message, cause);
    }
} 