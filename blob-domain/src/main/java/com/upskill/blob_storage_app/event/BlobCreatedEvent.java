package com.upskill.blob_storage_app.event;

import com.upskill.blob_storage_app.entity.Blob;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BlobCreatedEvent {
    private final Blob blob;
    private final LocalDateTime timestamp;

    public BlobCreatedEvent(Blob blob) {
        this.blob = blob;
        this.timestamp = LocalDateTime.now();
    }
} 