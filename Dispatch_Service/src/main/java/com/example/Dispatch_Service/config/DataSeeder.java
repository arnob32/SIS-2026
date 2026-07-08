package com.example.Dispatch_Service.config;

import com.example.Dispatch_Service.model.Priority;
import com.example.Dispatch_Service.service.DispatchService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seed(DispatchService dispatchService) {
        return args -> {
            if (!dispatchService.findAll().isEmpty()) {
                return;
            }
            var r1 = dispatchService.receiveReport("ISR-1001", "Large pothole on Hauptstraße",
                    "Deep pothole near house no. 14, risk for cyclists.", "Hauptstraße 14, Dortmund");
            dispatchService.prioritizeReport(r1.getId(), Priority.HIGH);

            dispatchService.receiveReport("ISR-1002", "Broken traffic light",
                    "Traffic light stuck on red at the Westfalendamm junction.", "Westfalendamm, Dortmund");

            var r3 = dispatchService.receiveReport("ISR-1003", "Missing stop sign",
                    "Stop sign knocked over after storm.", "Kirchderner Str., Dortmund");
            dispatchService.prioritizeReport(r3.getId(), Priority.CRITICAL);

            System.out.println("→ Seeded " + dispatchService.findAll().size() + " demo reports.");
        };
    }
}
