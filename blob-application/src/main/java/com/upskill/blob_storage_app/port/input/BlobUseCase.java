package com.upskill.blob_storage_app.port.input;

import com.upskill.blob_storage_app.valueobject.StorageProvider;
import java.util.List;
import java.util.UUID;

public interface BlobUseCase {
    String uploadBlob(UUID userId, byte[] content, String filename, StorageProvider provider);
    byte[] downloadBlob(UUID userId, String blobId, StorageProvider provider);
    void deleteBlob(UUID userId, String blobId, StorageProvider provider);
    List<String> listBlobs(UUID userId, StorageProvider provider);
} 