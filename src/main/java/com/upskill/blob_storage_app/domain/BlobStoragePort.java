package com.upskill.blob_storage_app.domain;

import java.util.List;

/**
 * Port interface defining the contract for blob storage operations.
 * 
 * This interface follows the Port-Adapter pattern (Hexagonal Architecture) to:
 * 1. Decouple the core domain logic from specific storage implementations
 * 2. Allow for easy swapping of storage backends (e.g S3, Azure Blob, Local FS)
 * 3. Enable testing without actual storage dependencies
 * 
 * The interface is intentionally kept simple and focused on core blob operations:
 * - save: Persists binary content at a specified path
 * - get: Retrieves binary content from a path
 * - list: Enumerates contents of a directory
 * - delete: Removes content at a specified path
 * 
 */
public interface BlobStoragePort {
    void save(String path, byte[] content);
    byte[] get(String path);
    List<String> list(String directory);
    void delete(String path);
} 