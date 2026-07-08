package com.example.Dispatch_Service.model;

import java.time.LocalDateTime;

/**
 * Domain event published when a report is resolved.
 * Sent to the FeedBack Service so the citizen can be invited to rate the resolution.
 */
public class ReportResolvedEvent {

    private String reportId;
    private LocalDateTime occurredAt;

    public ReportResolvedEvent() {}

    public ReportResolvedEvent(String reportId) {
        this.reportId = reportId;
        this.occurredAt = LocalDateTime.now();
    }

    public String getReportId() { return reportId; }
    public void setReportId(String reportId) { this.reportId = reportId; }

    public LocalDateTime getOccurredAt() { return occurredAt; }
    public void setOccurredAt(LocalDateTime occurredAt) { this.occurredAt = occurredAt; }
}
