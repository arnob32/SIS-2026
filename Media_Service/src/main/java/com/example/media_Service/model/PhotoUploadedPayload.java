package com.example.media_Service.model;

import java.time.LocalDateTime;

public class PhotoUploadedPayload {

    private String photoId;
    private String workOrderId;
    private String fileUrl;
    private LocalDateTime uploadedAt;

    public PhotoUploadedPayload() {}

    public PhotoUploadedPayload(String photoId, String workOrderId, String fileUrl) {
        this.photoId = photoId;
        this.workOrderId = workOrderId;
        this.fileUrl = fileUrl;
        this.uploadedAt = LocalDateTime.now();
    }

    public String getPhotoId() { return photoId; }
    public void setPhotoId(String photoId) { this.photoId = photoId; }

    public String getWorkOrderId() { return workOrderId; }
    public void setWorkOrderId(String workOrderId) { this.workOrderId = workOrderId; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
}
