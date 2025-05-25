package com.upskill.blob_storage_app.infrastructure;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.upskill.blob_storage_app.domain.BlobStorageException;
import com.upskill.blob_storage_app.domain.BlobStoragePort;

/**
 * Infrastructure adapter for local file system storage.
 * <p>
 * Implements {@link BlobStoragePort} to provide blob operations using the local disk.
 * This class is intended to be used by the domain/service layer via the port interface.
 */
public class LocalStorageAdapter implements BlobStoragePort {
    /** The root directory for all blob storage operations. */
    private final Path rootDirectory;

    /**
     * Constructs a LocalStorageAdapter with the given root directory.
     * @param rootDirectory The root directory for storage.
     */
    public LocalStorageAdapter(String rootDirectory) {
        this.rootDirectory = Paths.get(rootDirectory);
    }

    /**
     * Saves content to a file at the given path, creating parent directories as needed.
     * @param path Relative path under the root directory.
     * @param content File content as bytes.
     * @throws BlobStorageException if the file cannot be saved.
     */
    @Override
    public void save(String path, byte[] content) {
        try {
            Path filePath = rootDirectory.resolve(path);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, content);
        } catch (IOException e) {
            throw new BlobStorageException("Failed to save file: " + path, e);
        }
    }

    /**
     * Reads the content of a file at the given path.
     * @param path Relative path under the root directory.
     * @return File content as bytes.
     * @throws BlobStorageException if the file cannot be read.
     */
    @Override
    public byte[] get(String path) {
        try {
            Path filePath = rootDirectory.resolve(path);
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new BlobStorageException("Failed to read file: " + path, e);
        }
    }

    /**
     * Lists all files in the given directory.
     * @param directory Relative directory under the root directory.
     * @return List of file names in the directory.
     * @throws BlobStorageException if the directory cannot be listed.
     */
    @Override
    public List<String> list(String directory) {
        List<String> files = new ArrayList<>();
        try {
            Path dirPath = rootDirectory.resolve(directory);
            if (Files.exists(dirPath) && Files.isDirectory(dirPath)) {
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath)) {
                    for (Path entry : stream) {
                        files.add(entry.getFileName().toString());
                    }
                }
            }
        } catch (IOException e) {
            throw new BlobStorageException("Failed to list directory: " + directory, e);
        }
        return files;
    }

    /**
     * Deletes the file at the given path, if it exists.
     * @param path Relative path under the root directory.
     * @throws BlobStorageException if the file cannot be deleted.
     */
    @Override
    public void delete(String path) {
        try {
            Path filePath = rootDirectory.resolve(path);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new BlobStorageException("Failed to delete file: " + path, e);
        }
    }

    /**
     * Factory method to create a LocalStorageAdapter from a config map.
     * @param config Map with key "rootDir" for the root directory.
     * @return Configured LocalStorageAdapter instance.
     */
    public static LocalStorageAdapter fromConfig(java.util.Map<String, String> config) {
        return new LocalStorageAdapter(config.get("rootDir"));
    }
} 