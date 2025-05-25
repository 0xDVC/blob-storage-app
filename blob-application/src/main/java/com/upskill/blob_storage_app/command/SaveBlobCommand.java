package com.upskill.blob_storage_app.command;

import com.upskill.blob_storage_app.valueobject.StorageProvider;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class SaveBlobCommand {
    private final UUID userId;
    private final byte[] content;
    private final String filename;
    private final String contentType;
    private final StorageProvider storageProvider;
} 