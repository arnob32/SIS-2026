package com.example.media_Service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.media_Service.model.Photo;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, String> {

    List<Photo> findByWorkOrderId(String workOrderId);

    List<Photo> findByUploaderId(String uploaderId);

    List<Photo> findByActive(boolean active);
}
