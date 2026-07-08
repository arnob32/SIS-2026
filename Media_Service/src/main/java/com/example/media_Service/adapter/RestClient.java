package com.example.media_Service.adapter;

import org.springframework.stereotype.Component;

import com.example.media_Service.model.PhotoUploadedPayload;

@Component
public class RestClient {

    public void notifyPhotoUploaded(PhotoUploadedPayload payload) {
        try {
            System.out.println("→ Publishing PhotoUploaded: " + payload.getPhotoId()
                    + " for WorkOrder " + payload.getWorkOrderId());
            // When other services are ready:
            // restTemplate.postForObject(
            //     "http://NOTIFICATION-SERVICE/api/notifications/photo-uploaded",
            //     payload, Void.class);
        } catch (Exception e) {
            System.out.println("Could not notify: " + e.getMessage());
        }
    }

    public boolean checkWorkOrderExists(String workOrderId) {
        try {
            // Would call Dispatch Service:
            // return restTemplate.getForObject(
            //     "http://DISPATCH-SERVICE/api/workorders/" + workOrderId,
            //     Boolean.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
