package com.upskill.blob_storage_app.query;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class GetBlobQuery {
    private final UUID userId;
    private final String blobId;
} 