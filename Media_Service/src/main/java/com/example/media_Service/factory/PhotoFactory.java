package com.example.media_Service.factory;

import com.example.media_Service.model.FileType;
import com.example.media_Service.model.Photo;
import org.springframework.stereotype.Component;

/**
 * Factory for the Photo aggregate (per the Media / Photo Management tactical design).
 *
 * Keeps creation and validation logic out of the domain service and guarantees that
 * only valid Photo aggregates are produced.
 */
@Component
public class PhotoFactory {

    /**
     * Creates a valid Photo aggregate, validating the file before it is returned.
     *
     * @throws IllegalArgumentException if required data is missing or the file is invalid
     */
    public Photo create(String workOrderId, String uploaderId, String fileName,
                        String fileUrl, FileType fileType, Long fileSize, String description) {

        if (workOrderId == null || workOrderId.isBlank()) {
            throw new IllegalArgumentException("workOrderId is required");
        }
        if (uploaderId == null || uploaderId.isBlank()) {
            throw new IllegalArgumentException("uploaderId is required");
        }
        if (fileUrl == null || fileUrl.isBlank()) {
            throw new IllegalArgumentException("fileUrl is required");
        }

        Photo photo = new Photo(workOrderId, uploaderId, fileName, fileUrl,
                fileType, fileSize, description);

        if (!photo.validateFile()) {
            throw new IllegalArgumentException(
                    "Invalid file: type must be one of JPEG/PNG/WEBP and size <= 10 MB");
        }

        return photo;
    }
}
