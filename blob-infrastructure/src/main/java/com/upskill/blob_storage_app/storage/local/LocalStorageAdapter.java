package com.upskill.blob_storage_app.storage.local;

import com.upskill.blob_storage_app.port.output.BlobStoragePort;
import com.upskill.blob_storage_app.valueobject.StorageProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.upskill.blob_storage_app.exception.BlobStorageException;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LocalStorageAdapter implements BlobStoragePort {

    private static final Logger log = LoggerFactory.getLogger(LocalStorageAdapter.class);

    @Value("${storage.local.base-path}")
    private String basePath;

    @Override
    public String upload(byte[] content, String filename, String contentType, StorageProvider provider) {
        try {
            Path directory = createDirectoryIfNotExists();
            String key = generateKey(filename);
            Path filePath = directory.resolve(key);
            log.info("Uploading to local storage: {}", filePath);
            Files.write(filePath, content);
            log.info("Upload to local storage complete: {}", filePath);
            return key;
        } catch (IOException e) {
            log.error("Failed to upload to local storage", e);
            throw new BlobStorageException("Failed to upload file to local storage", e);
        }
    }

    @Override
    public byte[] download(String location, StorageProvider provider) {
        try {
            Path filePath = Paths.get(basePath, location);
            log.info("Downloading from local storage: {}", filePath);
            byte[] data = Files.readAllBytes(filePath);
            log.info("Download from local storage complete: {}", filePath);
            return data;
        } catch (IOException e) {
            log.error("Failed to download from local storage: {}", location, e);
            throw new BlobStorageException("Failed to download file from local storage", e);
        }
    }

    @Override
    public void delete(String location, StorageProvider provider) {
        try {
            Path filePath = Paths.get(basePath, location);
            log.info("Deleting from local storage: {}", filePath);
            Files.deleteIfExists(filePath);
            log.info("Delete from local storage complete: {}", filePath);
        } catch (IOException e) {
            log.error("Failed to delete from local storage: {}", location, e);
            throw new BlobStorageException("Failed to delete file from local storage", e);
        }
    }

    @Override
    public List<String> list(String prefix, StorageProvider provider) {
        try {
            Path directory = Paths.get(basePath);
            log.info("Listing local storage files in: {} with prefix {}", directory, prefix);
            List<String> files = Files.walk(directory)
                    .filter(Files::isRegularFile)
                    .map(directory::relativize)
                    .map(Path::toString)
                    .filter(path -> path.startsWith(prefix))
                    .collect(Collectors.toList());
            log.info("Local storage list complete: found {} files", files.size());
            return files;
        } catch (IOException e) {
            log.error("Failed to list files in local storage", e);
            throw new BlobStorageException("Failed to list files in local storage", e);
        }
    }

    @Override
    public String generatePresignedUrl(String location, StorageProvider provider, long expirationMinutes) {
        // Local storage doesn't support presigned URLs
        throw new UnsupportedOperationException("Presigned URLs are not supported for local storage");
    }

    private Path createDirectoryIfNotExists() throws IOException {
        Path directory = Paths.get(basePath);
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }
        return directory;
    }

    private String generateKey(String filename) {
        return String.format("%s/%s", java.util.UUID.randomUUID(), filename);
    }
} 