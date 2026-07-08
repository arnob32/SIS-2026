package com.example.Dispatch_Service.model;

/**
 * Value Object — lifecycle state of a Report within the Maintenance Dispatch context.
 * Lifecycle: SUBMITTED -> UNDER_REVIEW -> IN_PROGRESS -> RESOLVED
 * Alternative: SUBMITTED -> REJECTED
 */
public enum ReportStatus {
    SUBMITTED,
    UNDER_REVIEW,
    IN_PROGRESS,
    RESOLVED,
    REJECTED
}
