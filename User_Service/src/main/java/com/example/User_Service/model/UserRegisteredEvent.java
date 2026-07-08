package com.example.User_Service.model;

import java.time.LocalDateTime;
import com.example.User_Service.model.UserRole;

public class UserRegisteredEvent {

    private String userId;
    private String email;
    private UserRole role;
    private LocalDateTime occurredAt;

    public UserRegisteredEvent() {}

    public UserRegisteredEvent(String userId, String email, UserRole role) {
        this.userId = userId;
        this.email = email;
        this.role = role;
        this.occurredAt = LocalDateTime.now();
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    public LocalDateTime getOccurredAt() { return occurredAt; }
    public void setOccurredAt(LocalDateTime occurredAt) { this.occurredAt = occurredAt; }
}
