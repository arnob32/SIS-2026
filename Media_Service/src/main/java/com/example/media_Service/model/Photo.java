package com.example.media_Service.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "photos")
public class Photo {

    @Id
    private String photoId;

    @Column(nullable = false)
    private String workOrderId;

    @Column(nullable = false)
    private String uploaderId;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false, length = 1000)
    private String fileUrl;

    @Enumerated(EnumType.STRING)
    private FileType fileType;

    private Long fileSize;

    @Column(length = 500)
    private String description;

    private LocalDateTime uploadedAt;
    private boolean active = true;

    public Photo() {
        this.photoId = UUID.randomUUID().toString();
        this.uploadedAt = LocalDateTime.now();
    }

    public Photo(String workOrderId, String uploaderId, String fileName,
                 String fileUrl, FileType fileType, Long fileSize, String description) {
        this();
        this.workOrderId = workOrderId;
        this.uploaderId = uploaderId;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.description = description;
    }

    public boolean validateFile() {
        if (fileSize == null || fileSize > 10_000_000) return false; // 10 MB max
        return fileType != null;
    }

    public void remove() {
        this.active = false;
    }

    public boolean isAccessible() {
        return active && fileUrl != null && !fileUrl.isBlank();
    }

    // Getters and Setters
    public String getPhotoId() { return photoId; }
    public void setPhotoId(String photoId) { this.photoId = photoId; }

    public String getWorkOrderId() { return workOrderId; }
    public void setWorkOrderId(String workOrderId) { this.workOrderId = workOrderId; }

    public String getUploaderId() { return uploaderId; }
    public void setUploaderId(String uploaderId) { this.uploaderId = uploaderId; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

    public FileType getFileType() { return fileType; }
    public void setFileType(FileType fileType) { this.fileType = fileType; }

    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
