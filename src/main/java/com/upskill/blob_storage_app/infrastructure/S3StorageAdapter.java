package com.upskill.blob_storage_app.infrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.upskill.blob_storage_app.domain.BlobStorageException;
import com.upskill.blob_storage_app.domain.BlobStoragePort;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

/**
 * Infrastructure adapter for AWS S3 storage.
 *
 * Implements the BlobStoragePort interface as a concrete strategy for AWS S3.
 * This class is instantiated via the factory (Strategy Factory Pattern) to provide
 * blob operations for a specific S3 bucket and region, supporting multi-tenant use.
 */
public class S3StorageAdapter implements BlobStoragePort {
    private final S3Client s3Client;
    private final String bucketName;

    /**
     * Constructs an S3StorageAdapter with the given S3 client and bucket name.
     *
     * @param s3Client   The AWS S3 client to use
     * @param bucketName The S3 bucket name
     */
    public S3StorageAdapter(S3Client s3Client, String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    /**
     * Factory method to create an S3StorageAdapter from a configuration map.
     *
     * @param config Map containing 'region' and 'bucket' keys
     * @return Configured S3StorageAdapter instance
     */
    public static S3StorageAdapter fromConfig(Map<String, String> config) {
        Region region = Region.of(config.get("region"));
        S3Client client = S3Client.builder()
                .region(region)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
        return new S3StorageAdapter(client, config.get("bucket"));
    }

    /**
     * Saves a file to S3 at the specified path.
     *
     * @param path    The S3 object key (path)
     * @param content The file content as a byte array
     * @throws BlobStorageException if the operation fails
     */
    @Override
    public void save(String path, byte[] content) {
        try {
            PutObjectRequest req = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(path)
                    .build();
            s3Client.putObject(req, RequestBody.fromBytes(content));
        } catch (S3Exception e) {
            // Wrap S3-specific exception in a domain exception
            throw new BlobStorageException("Failed to save file to S3: " + path, e);
        }
    }

    /**
     * Retrieves a file from S3 at the specified path.
     *
     * @param path The S3 object key (path)
     * @return The file content as a byte array
     * @throws BlobStorageException if the operation fails
     */
    @Override
    public byte[] get(String path) {
        try {
            GetObjectRequest req = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(path)
                    .build();
            ResponseBytes<GetObjectResponse> resp = s3Client.getObjectAsBytes(req);
            return resp.asByteArray();
        } catch (S3Exception e) {
            throw new BlobStorageException("Failed to read file from S3: " + path, e);
        }
    }

    /**
     * Lists files in the specified S3 directory (prefix).
     *
     * @param directory The S3 prefix (directory)
     * @return List of file names (relative to the directory)
     * @throws BlobStorageException if the operation fails
     */
    @Override
    public List<String> list(String directory) {
        List<String> files = new ArrayList<>();
        try {
            // Ensure prefix ends with '/'
            ListObjectsV2Request req = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .prefix(directory.endsWith("/") ? directory : directory + "/")
                    .delimiter("/")
                    .build();
            ListObjectsV2Response resp = s3Client.listObjectsV2(req);
            resp.contents().forEach(obj -> {
                String key = obj.key();
                // Exclude directory placeholders
                if (!key.endsWith("/")) {
                    // Return file name relative to the directory
                    files.add(key.substring(directory.length() + 1));
                }
            });
        } catch (S3Exception e) {
            throw new BlobStorageException("Failed to list directory in S3: " + directory, e);
        }
        return files;
    }

    /**
     * Deletes a file from S3 at the specified path.
     *
     * @param path The S3 object key (path)
     * @throws BlobStorageException if the operation fails
     */
    @Override
    public void delete(String path) {
        try {
            DeleteObjectRequest req = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(path)
                    .build();
            s3Client.deleteObject(req);
        } catch (S3Exception e) {
            throw new BlobStorageException("Failed to delete file from S3: " + path, e);
        }
    }
} 