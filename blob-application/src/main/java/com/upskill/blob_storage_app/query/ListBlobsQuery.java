package com.upskill.blob_storage_app.query;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class ListBlobsQuery {
    private final UUID userId;
    private final int page;
    private final int size;
} 