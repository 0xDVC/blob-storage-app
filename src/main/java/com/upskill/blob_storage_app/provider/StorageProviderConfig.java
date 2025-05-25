package com.upskill.blob_storage_app.provider;

import java.util.Map;

public class StorageProviderConfig {
    private String storageProviderId;
    private String storageProviderType; // "local", "s3", etc.
    private Map<String, String> config; // e.g., bucket, path, credentials

    public String getStorageProviderId() {
        return storageProviderId;
    }
    public void setStorageProviderId(String storageProviderId) {
        this.storageProviderId = storageProviderId;
    }
    public String getStorageProviderType() {
        return storageProviderType;
    }
    public void setStorageProviderType(String storageProviderType) {
        this.storageProviderType = storageProviderType;
    }
    public Map<String, String> getConfig() {
        return config;
    }
    public void setConfig(Map<String, String> config) {
        this.config = config;
    }
} 