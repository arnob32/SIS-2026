package com.example.Dispatch_Service.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Integration beans for Task 2.
 *
 * The {@link LoadBalanced} RestTemplate resolves service names (e.g. "user-service")
 * through Eureka and client-side load balancing, so downstream calls use logical
 * service ids instead of hard-coded host:port.
 */
@Configuration
public class IntegrationConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
