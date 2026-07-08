package com.example.User_Service.model;

import java.time.LocalDateTime;

public class UserDeactivatedEvent {

    private String userId;
    private LocalDateTime occurredAt;

    public UserDeactivatedEvent() {}

    public UserDeactivatedEvent(String userId) {
        this.userId = userId;
        this.occurredAt = LocalDateTime.now();
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public LocalDateTime getOccurredAt() { return occurredAt; }
    public void setOccurredAt(LocalDateTime occurredAt) { this.occurredAt = occurredAt; }
}
