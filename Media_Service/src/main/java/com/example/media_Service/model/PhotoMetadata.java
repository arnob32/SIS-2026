package com.example.media_Service.model;

/**
 * Read-only metadata payload for a Photo, exposed to other contexts (e.g. Issue
 * Reporting / Maintenance Dispatch) that need to display or reference an image
 * without owning the media data. Part of the Media context's published language.
 */
public class PhotoMetadata {

    private String photoId;
    private String workOrderId;
    private String uploaderId;
    private String fileName;
    private String fileUrl;
    private String fileType;
    private Long fileSize;
    private boolean active;

    public PhotoMetadata() {}

    public PhotoMetadata(Photo photo) {
        this.photoId = photo.getPhotoId();
        this.workOrderId = photo.getWorkOrderId();
        this.uploaderId = photo.getUploaderId();
        this.fileName = photo.getFileName();
        this.fileUrl = photo.getFileUrl();
        this.fileType = photo.getFileType() != null ? photo.getFileType().name() : null;
        this.fileSize = photo.getFileSize();
        this.active = photo.isActive();
    }

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

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
