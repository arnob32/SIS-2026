package com.example.Dispatch_Service.service;

import com.example.Dispatch_Service.adapter.RestClient;
import com.example.Dispatch_Service.model.*;
import com.example.Dispatch_Service.repository.ReportRepository;
import com.example.Dispatch_Service.repository.StatusHistoryRepository;
import com.example.Dispatch_Service.repository.WorkAssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Domain Service of the Maintenance Dispatch context.
 *
 * Encapsulates business processes that do not naturally belong to a single entity:
 * receiving reports from Issue Reporting, prioritisation, work assignment, status
 * transitions and resolution. It coordinates the Report aggregate, the repositories
 * and the outbound RestClient adapter, and records a StatusHistory entry for every
 * lifecycle transition.
 */
@Service
public class DispatchService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private WorkAssignmentRepository workAssignmentRepository;

    @Autowired
    private StatusHistoryRepository statusHistoryRepository;

    @Autowired
    private RestClient restClient;

    private static final List<ReportStatus> TERMINAL =
            List.of(ReportStatus.RESOLVED, ReportStatus.REJECTED);

    /**
     * Ingests a report handed over from the Issue Reporting context.
     * (In Task 2 this is driven by a ReportSubmittedEvent; here it is invoked directly.)
     */
    public Report receiveReport(String sourceReportId, String title, String description, String location) {
        Report report = new Report(sourceReportId, title, description, location);
        return reportRepository.save(report);
    }

    /** Assigns a report to a maintenance team/worker and moves it to IN_PROGRESS. */
    public Report assignReport(String reportId, String assignedTo, LocalDateTime dueDate) {
        Report report = findById(reportId);

        WorkAssignment assignment = new WorkAssignment(reportId, assignedTo, dueDate);
        assignment.assign();
        workAssignmentRepository.save(assignment);

        ReportStatus previous = report.getStatus();
        report.assignWork();
        reportRepository.save(report);

        recordAndPublish(report, previous, "AUTHORITY");
        return report;
    }

    /** Updates the urgency level of a report. */
    public Report prioritizeReport(String reportId, Priority priority) {
        Report report = findById(reportId);
        report.setPriority(priority);
        return reportRepository.save(report);
    }

    /** Transitions a report to a new status, recording history and notifying downstream. */
    public Report updateReportStatus(String reportId, ReportStatus newStatus, String changedBy) {
        Report report = findById(reportId);
        ReportStatus previous = report.getStatus();
        report.changeStatus(newStatus);
        reportRepository.save(report);

        recordAndPublish(report, previous, changedBy);

        if (newStatus == ReportStatus.RESOLVED) {
            restClient.postReportResolved(new ReportResolvedEvent(reportId));
        }
        return report;
    }

    /** Marks a report resolved, records history, and notifies Notification + FeedBack. */
    public Report resolveReport(String reportId, String changedBy) {
        Report report = findById(reportId);
        ReportStatus previous = report.getStatus();
        report.resolveReport();
        reportRepository.save(report);

        recordAndPublish(report, previous, changedBy);
        restClient.postReportResolved(new ReportResolvedEvent(reportId));
        return report;
    }

    /** Rejects a report, recording history and notifying Notification. */
    public Report rejectReport(String reportId, String changedBy) {
        Report report = findById(reportId);
        ReportStatus previous = report.getStatus();
        report.rejectReport();
        reportRepository.save(report);

        recordAndPublish(report, previous, changedBy);
        return report;
    }

    private void recordAndPublish(Report report, ReportStatus previous, String changedBy) {
        statusHistoryRepository.save(
                new StatusHistory(report.getId(), previous, report.getStatus(), changedBy));
        restClient.postStatusChanged(
                new ReportStatusChangedEvent(report.getId(), previous, report.getStatus()));
    }

    // ----- Queries -----

    public Report findById(String reportId) {
        return reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found: " + reportId));
    }

    public List<Report> findAll() {
        return reportRepository.findAll();
    }

    public List<Report> findByStatus(ReportStatus status) {
        return reportRepository.findByStatus(status);
    }

    public List<Report> findAllOpenReports() {
        return reportRepository.findByStatusNotIn(TERMINAL);
    }

    public List<WorkAssignment> findAssignments(String reportId) {
        return workAssignmentRepository.findByReportId(reportId);
    }

    public List<StatusHistory> findHistory(String reportId) {
        return statusHistoryRepository.findByReportId(reportId);
    }
}
