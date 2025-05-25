package com.upskill.blob_storage_app.exception;

public class UserNotFoundException extends BlobStorageException {
    public UserNotFoundException(String userId) {
        super("User not found: " + userId);
    }
} 