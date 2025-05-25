package com.upskill.blob_storage_app.exception;

public class BlobNotFoundException extends BlobStorageException {
    public BlobNotFoundException(String blobId) {
        super("Blob not found: " + blobId);
    }
} 