package com.upskill.blob_storage_app.config;

import com.upskill.blob_storage_app.port.output.BlobStoragePort;
import com.upskill.blob_storage_app.port.output.BlobStorageStrategyFactory;
import com.upskill.blob_storage_app.storage.s3.S3StorageAdapter;
import com.upskill.blob_storage_app.storage.local.LocalStorageAdapter;
import com.upskill.blob_storage_app.valueobject.StorageProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import jakarta.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class BlobStorageStrategyFactoryImpl implements BlobStorageStrategyFactory {
    private final S3StorageAdapter s3StorageAdapter;
    private final LocalStorageAdapter localStorageAdapter;
    // Add more adapters as needed

    private final Map<StorageProvider, BlobStoragePort> strategies = new EnumMap<>(StorageProvider.class);

    @PostConstruct
    public void init() {
        strategies.put(StorageProvider.S3, s3StorageAdapter);
        strategies.put(StorageProvider.LOCAL, localStorageAdapter);
        // Add more as needed
    }

    @Override
    public BlobStoragePort getStrategy(StorageProvider provider) {
        BlobStoragePort port = strategies.get(provider);
        if (port == null) throw new IllegalArgumentException("No adapter for provider: " + provider);
        return port;
    }
} 