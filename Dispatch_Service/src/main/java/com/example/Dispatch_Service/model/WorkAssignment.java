package com.example.Dispatch_Service.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity within the Report aggregate boundary.
 * Represents the assignment of a report to a maintenance team or responsible worker.
 */
@Entity
@Table(name = "work_assignments")
public class WorkAssignment {

    @Id
    private String id;

    /** The Report this assignment belongs to. */
    private String reportId;

    /** Identifier/name of the technician or team the work is assigned to. */
    private String assignedTo;

    private LocalDateTime assignedAt;
    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    private AssignmentStatus status;

    public WorkAssignment() {
        this.id = UUID.randomUUID().toString();
        this.status = AssignmentStatus.PENDING;
    }

    public WorkAssignment(String reportId, String assignedTo, LocalDateTime dueDate) {
        this();
        this.reportId = reportId;
        this.assignedTo = assignedTo;
        this.dueDate = dueDate;
    }

    // ----- Business methods (per UML) -----

    public void assign() {
        this.assignedAt = LocalDateTime.now();
        this.status = AssignmentStatus.ASSIGNED;
    }

    public void complete() {
        this.status = AssignmentStatus.COMPLETED;
    }

    public void cancel() {
        this.status = AssignmentStatus.CANCELLED;
    }

    // ----- Getters / setters -----

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getReportId() { return reportId; }
    public void setReportId(String reportId) { this.reportId = reportId; }

    public String getAssignedTo() { return assignedTo; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }

    public LocalDateTime getAssignedAt() { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }

    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }

    public AssignmentStatus getStatus() { return status; }
    public void setStatus(AssignmentStatus status) { this.status = status; }
}
