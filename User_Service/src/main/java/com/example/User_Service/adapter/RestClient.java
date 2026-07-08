package com.example.User_Service.adapter;

import com.example.User_Service.model.UserDeactivatedEvent;
import com.example.User_Service.model.UserRegisteredEvent;
import org.springframework.stereotype.Component;

@Component
public class RestClient {

    public void postUserRegistered(UserRegisteredEvent event) {    
        try {
            System.out.println("→ Publishing UserRegistered: " + event.getEmail());
         
        } catch (Exception e) {
            System.out.println("Could not notify: " + e.getMessage());
        }
    }

    public void postUserDeactivated(UserDeactivatedEvent event) {
        try {
            System.out.println("→ Publishing UserDeactivated: " + event.getUserId());
        } catch (Exception e) {
            System.out.println("Could not notify: " + e.getMessage());
        }
    }
}