package com.upskill.blob_storage_app.port.output;

import com.upskill.blob_storage_app.valueobject.StorageProvider;

import java.util.List;

public interface BlobStoragePort {
    String upload(byte[] content, String filename, String contentType, StorageProvider provider);
    byte[] download(String location, StorageProvider provider);
    void delete(String location, StorageProvider provider);
    List<String> list(String prefix, StorageProvider provider);
    String generatePresignedUrl(String location, StorageProvider provider, long expirationMinutes);
} 