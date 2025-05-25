package com.upskill.blob_storage_app.service;

import com.upskill.blob_storage_app.command.DeleteBlobCommand;
import com.upskill.blob_storage_app.command.SaveBlobCommand;
import com.upskill.blob_storage_app.entity.Blob;
import com.upskill.blob_storage_app.entity.User;
import com.upskill.blob_storage_app.port.input.BlobUseCase;
import com.upskill.blob_storage_app.port.output.BlobStoragePort;
import com.upskill.blob_storage_app.query.GetBlobQuery;
import com.upskill.blob_storage_app.query.ListBlobsQuery;
import com.upskill.blob_storage_app.repository.BlobRepository;
import com.upskill.blob_storage_app.repository.UserRepository;
import com.upskill.blob_storage_app.service.BlobDomainService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

import java.util.List;
import java.util.stream.Collectors;
import com.upskill.blob_storage_app.port.output.BlobStorageStrategyFactory;
import com.upskill.blob_storage_app.valueobject.StorageProvider;
import com.upskill.blob_storage_app.exception.BlobNotFoundException;
import com.upskill.blob_storage_app.exception.UnauthorizedBlobAccessException;
import com.upskill.blob_storage_app.exception.UserNotFoundException;

@Service
@RequiredArgsConstructor
public class BlobService implements BlobUseCase {
    private static final Logger log = LoggerFactory.getLogger(BlobService.class);
    private final BlobRepository blobRepository;
    private final UserRepository userRepository;
    private final BlobStorageStrategyFactory storageFactory;
    private final BlobDomainService blobDomainService;

    @Override
    @Transactional
    public String uploadBlob(UUID userId, byte[] content, String filename, StorageProvider provider) {
        log.info("Uploading blob for user {} with provider {}", userId, provider);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User not found: {}", userId);
                    return new UserNotFoundException(userId.toString());
                });
        BlobStoragePort storage = storageFactory.getStrategy(provider);
        String storageLocation = storage.upload(content, filename, "application/octet-stream", provider);
        Blob blob = blobDomainService.createBlob(user, filename, "application/octet-stream", (long) content.length, provider, storageLocation);
        blobRepository.save(blob);
        log.info("Blob uploaded: {}", blob.getBlobId().getValue());
        return blob.getBlobId().getValue();
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] downloadBlob(UUID userId, String blobId, StorageProvider provider) {
        log.info("Downloading blob {} for user {} with provider {}", blobId, userId, provider);
        Blob blob = blobRepository.findByBlobId_Value(blobId)
                .orElseThrow(() -> {
                    log.warn("Blob not found: {}", blobId);
                    return new BlobNotFoundException(blobId);
                });
        if (!blob.getUser().getId().equals(userId)) {
            log.warn("Unauthorized access to blob {} by user {}", blobId, userId);
            throw new UnauthorizedBlobAccessException(blobId, userId.toString());
        }
        BlobStoragePort storage = storageFactory.getStrategy(provider);
        return storage.download(blob.getStorageLocation(), blob.getStorageProvider());
    }

    @Override
    @Transactional
    public void deleteBlob(UUID userId, String blobId, StorageProvider provider) {
        log.info("Deleting blob {} for user {} with provider {}", blobId, userId, provider);
        Blob blob = blobRepository.findByBlobId_Value(blobId)
                .orElseThrow(() -> {
                    log.warn("Blob not found: {}", blobId);
                    return new BlobNotFoundException(blobId);
                });
        if (!blob.getUser().getId().equals(userId)) {
            log.warn("Unauthorized delete attempt for blob {} by user {}", blobId, userId);
            throw new UnauthorizedBlobAccessException(blobId, userId.toString());
        }
        BlobStoragePort storage = storageFactory.getStrategy(provider);
        storage.delete(blob.getStorageLocation(), blob.getStorageProvider());
        blobDomainService.deleteBlob(blob);
        blobRepository.delete(blob);
        log.info("Blob deleted: {}", blobId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> listBlobs(UUID userId, StorageProvider provider) {
        log.info("Listing blobs for user {} with provider {}", userId, provider);
        // Optionally use provider for filtering or listing from the correct backend
        return blobRepository.findByUserId(userId).stream()
                .map(blob -> blob.getBlobId().getValue())
                .collect(Collectors.toList());
    }
} 