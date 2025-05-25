package com.upskill.blob_storage_app.command;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class DeleteBlobCommand {
    private final UUID userId;
    private final String blobId;
} 