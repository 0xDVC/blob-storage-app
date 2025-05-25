package com.upskill.blob_storage_app.port.output;

import com.upskill.blob_storage_app.valueobject.StorageProvider;

public interface BlobStorageStrategyFactory {
    BlobStoragePort getStrategy(StorageProvider provider);
} 