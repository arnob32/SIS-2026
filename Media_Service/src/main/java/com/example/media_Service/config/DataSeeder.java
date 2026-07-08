package com.example.media_Service.config;

import com.example.media_Service.model.FileType;
import com.example.media_Service.service.MediaService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Seeds a few demo photos so the UI and API have data to show during the demonstration.
 */
@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedMedia(MediaService mediaService) {
        return args -> {
            if (!mediaService.getAllPhotos().isEmpty()) {
                return;
            }
            mediaService.uploadPhoto("WO-1001", "citizen-anna",
                    "pothole_hauptstrasse.jpg",
                    "https://media.local/photos/pothole_hauptstrasse.jpg",
                    FileType.JPEG, 2_400_000L, "Pothole near Hauptstraße 14");

            mediaService.uploadPhoto("WO-1002", "citizen-ben",
                    "broken_light.png",
                    "https://media.local/photos/broken_light.png",
                    FileType.PNG, 1_100_000L, "Broken traffic light at Westfalendamm");

            mediaService.uploadPhoto("WO-1003", "technician-tom",
                    "signage_repaired.webp",
                    "https://media.local/photos/signage_repaired.webp",
                    FileType.WEBP, 640_000L, "Repaired stop sign, after work");

            System.out.println("-> Seeded " + mediaService.getAllPhotos().size() + " demo photos.");
        };
    }
}
