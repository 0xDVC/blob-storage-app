package com.upskill.blob_storage_app.exception;

public class UnauthorizedBlobAccessException extends BlobStorageException {
    public UnauthorizedBlobAccessException(String blobId, String userId) {
        super("User " + userId + " is not authorized to access blob: " + blobId);
    }
} 