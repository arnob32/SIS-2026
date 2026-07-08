package com.example.Dispatch_Service.adapter;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * Inbound integration with the Identity & Access context (User_Service).
 *
 * Maintenance Dispatch is a downstream Customer of User_Service: when assigning work
 * it needs the list of technicians. The call is guarded by a Resilience4j circuit
 * breaker — if User_Service is unavailable, the breaker trips and {@link #technicianFallback}
 * returns a cached/mock list so the dispatch workflow keeps functioning.
 */
@Component
public class UserServiceClient {

    private static final String USER_SERVICE_URL = "http://user-service/api/users/role/TECHNICIAN";

    private final RestTemplate restTemplate;

    public UserServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @CircuitBreaker(name = "userService", fallbackMethod = "technicianFallback")
    public List<Map<String, Object>> getTechnicians() {
        return restTemplate.exchange(
                USER_SERVICE_URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        ).getBody();
    }

    /**
     * Fallback used when User_Service is down or the circuit is open.
     * Returns a static technician list so assignment can still proceed.
     */
    @SuppressWarnings("unused")
    public List<Map<String, Object>> technicianFallback(Throwable t) {
        System.out.println("⚠ user-service unavailable (" + t.getClass().getSimpleName()
                + ") — returning fallback technicians");
        return List.of(
                Map.of("userId", "fallback-1", "fullName", "Tom Becker (cached)",
                        "specialization", "Potholes", "district", "Nord", "source", "fallback"),
                Map.of("userId", "fallback-2", "fullName", "Lena Vogt (cached)",
                        "specialization", "Traffic Lights", "district", "Süd", "source", "fallback"));
    }
}
