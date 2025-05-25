package com.upskill.blob_storage_app.storage.s3;

import com.upskill.blob_storage_app.port.output.BlobStoragePort;
import com.upskill.blob_storage_app.valueobject.StorageProvider;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import com.upskill.blob_storage_app.exception.BlobStorageException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class S3StorageAdapter implements BlobStoragePort {

    private static final Logger log = LoggerFactory.getLogger(S3StorageAdapter.class);

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Override
    public String upload(byte[] content, String filename, String contentType, StorageProvider provider) {
        String key = generateKey(filename);
        log.info("Uploading to S3: bucket={}, key={}, contentType={}", bucketName, key, contentType);
        try {
            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(contentType)
                    .build();
            s3Client.putObject(putRequest, RequestBody.fromBytes(content));
            log.info("Upload to S3 complete: key={}", key);
            return key;
        } catch (Exception e) {
            log.error("Failed to upload to S3: key={}", key, e);
            throw new BlobStorageException("Failed to upload file to S3", e);
        }
    }

    @Override
    public byte[] download(String location, StorageProvider provider) {
        log.info("Downloading from S3: bucket={}, key={}", bucketName, location);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            GetObjectRequest getRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(location)
                    .build();
            s3Client.getObject(getRequest).transferTo(baos);
            log.info("Download from S3 complete: key={}", location);
            return baos.toByteArray();
        } catch (Exception e) {
            log.error("Failed to download from S3: key={}", location, e);
            throw new BlobStorageException("Failed to download file from S3", e);
        }
    }

    @Override
    public void delete(String location, StorageProvider provider) {
        log.info("Deleting from S3: bucket={}, key={}", bucketName, location);
        try {
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(location)
                    .build();
            s3Client.deleteObject(deleteRequest);
            log.info("Delete from S3 complete: key={}", location);
        } catch (Exception e) {
            log.error("Failed to delete from S3: key={}", location, e);
            throw new BlobStorageException("Failed to delete file from S3", e);
        }
    }

    @Override
    public List<String> list(String prefix, StorageProvider provider) {
        log.info("Listing S3 objects: bucket={}, prefix={}", bucketName, prefix);
        try {
            ListObjectsV2Request request = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .prefix(prefix)
                    .build();
            ListObjectsV2Response response = s3Client.listObjectsV2(request);
            List<String> keys = response.contents().stream()
                    .map(S3Object::key)
                    .collect(Collectors.toList());
            log.info("S3 list complete: found {} objects", keys.size());
            return keys;
        } catch (Exception e) {
            log.error("Failed to list S3 objects: prefix={}", prefix, e);
            throw new BlobStorageException("Failed to list files in S3", e);
        }
    }

    @Override
    public String generatePresignedUrl(String location, StorageProvider provider, long expirationMinutes) {
        log.info("Generating S3 presigned URL: bucket={}, key={}, expiration={}min", bucketName, location, expirationMinutes);
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(location)
                    .build();
            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(expirationMinutes))
                    .getObjectRequest(getObjectRequest)
                    .build();
            PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
            String url = presignedRequest.url().toString();
            log.info("Generated S3 presigned URL: {}", url);
            return url;
        } catch (Exception e) {
            log.error("Failed to generate S3 presigned URL: key={}", location, e);
            throw new BlobStorageException("Failed to generate presigned URL for S3", e);
        }
    }

    private String generateKey(String filename) {
        return String.format("%s/%s", java.util.UUID.randomUUID(), filename);
    }
} 