package com.example.Dispatch_Service.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity within the Report aggregate boundary.
 * Stores an audit trail of all report status changes.
 */
@Entity
@Table(name = "status_history")
public class StatusHistory {

    @Id
    private String id;

    private String reportId;

    @Enumerated(EnumType.STRING)
    private ReportStatus previousStatus;

    @Enumerated(EnumType.STRING)
    private ReportStatus newStatus;

    private LocalDateTime changedAt;
    private String changedBy;

    public StatusHistory() {
        this.id = UUID.randomUUID().toString();
    }

    /**
     * Records a status transition. Factory-style constructor matching the UML
     * operation {@code recordTransition()}.
     */
    public StatusHistory(String reportId, ReportStatus previousStatus,
                         ReportStatus newStatus, String changedBy) {
        this();
        this.reportId = reportId;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.changedBy = changedBy;
        this.changedAt = LocalDateTime.now();
    }

    // ----- Getters / setters -----

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getReportId() { return reportId; }
    public void setReportId(String reportId) { this.reportId = reportId; }

    public ReportStatus getPreviousStatus() { return previousStatus; }
    public void setPreviousStatus(ReportStatus previousStatus) { this.previousStatus = previousStatus; }

    public ReportStatus getNewStatus() { return newStatus; }
    public void setNewStatus(ReportStatus newStatus) { this.newStatus = newStatus; }

    public LocalDateTime getChangedAt() { return changedAt; }
    public void setChangedAt(LocalDateTime changedAt) { this.changedAt = changedAt; }

    public String getChangedBy() { return changedBy; }
    public void setChangedBy(String changedBy) { this.changedBy = changedBy; }
}
