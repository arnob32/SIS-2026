package com.example.Dispatch_Service.model;

import java.time.LocalDateTime;

/**
 * Domain event published when a report moves through its lifecycle.
 * Sent to the Notification Service so it can inform the citizen of the change.
 */
public class ReportStatusChangedEvent {

    private String reportId;
    private ReportStatus previousStatus;
    private ReportStatus newStatus;
    private LocalDateTime occurredAt;

    public ReportStatusChangedEvent() {}

    public ReportStatusChangedEvent(String reportId, ReportStatus previousStatus, ReportStatus newStatus) {
        this.reportId = reportId;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.occurredAt = LocalDateTime.now();
    }

    public String getReportId() { return reportId; }
    public void setReportId(String reportId) { this.reportId = reportId; }

    public ReportStatus getPreviousStatus() { return previousStatus; }
    public void setPreviousStatus(ReportStatus previousStatus) { this.previousStatus = previousStatus; }

    public ReportStatus getNewStatus() { return newStatus; }
    public void setNewStatus(ReportStatus newStatus) { this.newStatus = newStatus; }

    public LocalDateTime getOccurredAt() { return occurredAt; }
    public void setOccurredAt(LocalDateTime occurredAt) { this.occurredAt = occurredAt; }
}
