package com.example.media_Service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.media_Service.adapter.RestClient;
import com.example.media_Service.factory.PhotoFactory;
import com.example.media_Service.model.FileType;
import com.example.media_Service.model.Photo;
import com.example.media_Service.model.PhotoMetadata;
import com.example.media_Service.model.PhotoUploadedPayload;
import com.example.media_Service.repository.PhotoRepository;

@Service
public class MediaService {

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private PhotoFactory photoFactory;

    @Autowired
    private RestClient restClient;

    public Photo uploadPhoto(String workOrderId, String uploaderId, String fileName,
                             String fileUrl, FileType fileType, Long fileSize,
                             String description) {

        // The factory validates the file and produces a valid Photo aggregate.
        Photo photo = photoFactory.create(workOrderId, uploaderId, fileName, fileUrl,
                fileType, fileSize, description);

        photoRepository.save(photo);

        // Notify other services via REST
        PhotoUploadedPayload payload = new PhotoUploadedPayload(
                photo.getPhotoId(), workOrderId, fileUrl);
        restClient.notifyPhotoUploaded(payload);

        return photo;
    }

    public Photo getPhotoById(String photoId) {
        return photoRepository.findById(photoId)
                .orElseThrow(() -> new RuntimeException("Photo not found"));
    }

    public List<Photo> getPhotosByWorkOrder(String workOrderId) {
        return photoRepository.findByWorkOrderId(workOrderId);
    }

    public List<Photo> getPhotosByUploader(String uploaderId) {
        return photoRepository.findByUploaderId(uploaderId);
    }

    public void removePhoto(String photoId) {
        Photo photo = getPhotoById(photoId);
        photo.remove();
        photoRepository.save(photo);
    }

    public void deletePhoto(String photoId) {
        photoRepository.deleteById(photoId);
    }

    public List<Photo> getAllPhotos() {
        return photoRepository.findAll();
    }

    /** Read-only metadata for another context (published language). */
    public PhotoMetadata getMetadata(String photoId) {
        return new PhotoMetadata(getPhotoById(photoId));
    }
}
