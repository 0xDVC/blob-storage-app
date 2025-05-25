package com.upskill.blob_storage_app.service;

import com.upskill.blob_storage_app.entity.Blob;
import com.upskill.blob_storage_app.entity.User;
import com.upskill.blob_storage_app.event.BlobCreatedEvent;
import com.upskill.blob_storage_app.event.BlobDeletedEvent;
import com.upskill.blob_storage_app.valueobject.BlobId;
import com.upskill.blob_storage_app.valueobject.StorageProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlobDomainService {
    private final ApplicationEventPublisher eventPublisher;

    public Blob createBlob(User user, String filename, String contentType, Long size, 
                          StorageProvider provider, String storageLocation) {
        Blob blob = new Blob();
        blob.setBlobId(BlobId.generate());
        blob.setFilename(filename);
        blob.setContentType(contentType);
        blob.setSize(size);
        blob.setUser(user);
        blob.setStorageProvider(provider);
        blob.setStorageLocation(storageLocation);

        eventPublisher.publishEvent(new BlobCreatedEvent(blob));
        return blob;
    }

    public void deleteBlob(Blob blob) {
        eventPublisher.publishEvent(new BlobDeletedEvent(blob));
    }
} 