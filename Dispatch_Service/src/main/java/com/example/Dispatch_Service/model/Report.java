package com.example.Dispatch_Service.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Aggregate Root of the Maintenance Dispatch bounded context.
 *
 * A Report represents a road infrastructure issue being processed by the authority.
 * It originates in the Issue Reporting context (referenced via {@link #sourceReportId})
 * and is handled here through its dispatch lifecycle. The aggregate enforces all valid
 * status transitions; WorkAssignment and StatusHistory are managed within its boundary.
 */
@Entity
@Table(name = "reports")
public class Report {

    @Id
    private String id;

    /** Reference to the originating report in the Issue Reporting context. */
    private String sourceReportId;

    // Descriptive fields carried over from Issue Reporting (for display / demo).
    private String title;

    @Column(length = 2000)
    private String description;

    private String location;

    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    private LocalDateTime submittedAt;
    private LocalDateTime updatedAt;

    public Report() {
        this.id = UUID.randomUUID().toString();
        this.status = ReportStatus.SUBMITTED;
        this.priority = Priority.MEDIUM;
        this.submittedAt = LocalDateTime.now();
        this.updatedAt = this.submittedAt;
    }

    public Report(String sourceReportId, String title, String description, String location) {
        this();
        this.sourceReportId = sourceReportId;
        this.title = title;
        this.description = description;
        this.location = location;
    }

    // ----- Business methods (per UML) -----

    /** Marks the report as taken into processing once work has been assigned. */
    public void assignWork() {
        if (isClosed()) {
            throw new IllegalStateException("Cannot assign work to a " + status + " report");
        }
        this.status = ReportStatus.IN_PROGRESS;
        touch();
    }

    /** Transitions the report to a new lifecycle state, guarding illegal transitions. */
    public void changeStatus(ReportStatus newStatus) {
        if (isClosed()) {
            throw new IllegalStateException("Report is already " + status + " and cannot change");
        }
        this.status = newStatus;
        touch();
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
        touch();
    }

    public void resolveReport() {
        if (status == ReportStatus.REJECTED) {
            throw new IllegalStateException("A rejected report cannot be resolved");
        }
        this.status = ReportStatus.RESOLVED;
        touch();
    }

    public void rejectReport() {
        if (status == ReportStatus.RESOLVED) {
            throw new IllegalStateException("A resolved report cannot be rejected");
        }
        this.status = ReportStatus.REJECTED;
        touch();
    }

    private boolean isClosed() {
        return status == ReportStatus.RESOLVED || status == ReportStatus.REJECTED;
    }

    private void touch() {
        this.updatedAt = LocalDateTime.now();
    }

    // ----- Getters / setters -----

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSourceReportId() { return sourceReportId; }
    public void setSourceReportId(String sourceReportId) { this.sourceReportId = sourceReportId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public ReportStatus getStatus() { return status; }
    public void setStatus(ReportStatus status) { this.status = status; }

    public Priority getPriority() { return priority; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
