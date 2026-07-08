package com.example.Dispatch_Service.adapter;

import com.example.Dispatch_Service.model.ReportResolvedEvent;
import com.example.Dispatch_Service.model.ReportStatusChangedEvent;
import org.springframework.stereotype.Component;

/**
 * Outbound communication adapter.
 *
 * In the context map, Maintenance Dispatch is the Publisher to Notification (status
 * updates) and FeedBack (resolution). These calls would be synchronous HTTP POSTs to
 * the downstream services. For Lab Assignment 5 Task 1 the integration is mocked with
 * console output (real wiring + Resilience4j circuit breaker follow in Task 2).
 */
@Component
public class RestClient {

    /** POST the status change to the Notification Service. */
    public void postStatusChanged(ReportStatusChangedEvent event) {
        try {
            System.out.println("→ [Notification] ReportStatusChanged: report=" + event.getReportId()
                    + " " + event.getPreviousStatus() + " -> " + event.getNewStatus());
        } catch (Exception e) {
            System.out.println("Could not notify Notification Service: " + e.getMessage());
        }
    }

    /** POST the resolution to the FeedBack Service. */
    public void postReportResolved(ReportResolvedEvent event) {
        try {
            System.out.println("→ [FeedBack] ReportResolved: report=" + event.getReportId());
        } catch (Exception e) {
            System.out.println("Could not notify FeedBack Service: " + e.getMessage());
        }
    }
}
