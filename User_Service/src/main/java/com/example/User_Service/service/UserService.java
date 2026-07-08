package com.example.User_Service.service;

import com.example.User_Service.model.User;
import com.example.User_Service.model.UserRegisteredEvent;
import com.example.User_Service.model.UserDeactivatedEvent;
import com.example.User_Service.model.UserRole;
import com.example.User_Service.adapter.RestClient;
import com.example.User_Service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RestClient restClient;

    public User register(String fullName, String email, String rawPassword, UserRole role) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already registered: " + email);
        }

        String hashed = passwordEncoder.encode(rawPassword);
        User user = new User(fullName, email, hashed, role);
        userRepository.save(user);

        UserRegisteredEvent event = new UserRegisteredEvent(
                user.getUserId(), user.getEmail(), user.getRole());
        restClient.postUserRegistered(event);

        return user;
    }

    public User updateUser(String userId, String name, String phone, String city) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.updateInfo(name, phone, city);
        return userRepository.save(user);
    }

    public void updateTechnicianFields(String userId, String name, String phone,
                                       String city, String specialization, String district) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.updateInfo(name, phone, city);
        user.setSpecialization(specialization);
        user.setDistrict(district);
        userRepository.save(user);
    }

    public void deactivateUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.deactivate();
        userRepository.save(user);

        UserDeactivatedEvent event = new UserDeactivatedEvent(user.getUserId());
        restClient.postUserDeactivated(event);
    }

    public void deleteUser(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(userId);
    }

    public User findById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findByRole(UserRole role) {
        return userRepository.findByRole(role);
    }
}
