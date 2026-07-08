package com.example.Dispatch_Service.Controller;

import com.example.Dispatch_Service.adapter.UserServiceClient;
import com.example.Dispatch_Service.model.*;
import com.example.Dispatch_Service.service.DispatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * REST API for the Maintenance Dispatch context (Spring MVC, mandatory per Assignment 5).
 * Backed by a static demo UI at "/" (src/main/resources/static/index.html).
 */
@RestController
@RequestMapping("/api")
public class DispatchController {

    @Autowired
    private DispatchService dispatchService;

    @Autowired
    private UserServiceClient userServiceClient;

    /** Externalized property served by the Spring Cloud Config Server (Task 2). */
    @Value("${dispatch.info-message:(config server not connected)}")
    private String infoMessage;

    /** Shows whether centralized configuration was loaded from the Config Server. */
    @GetMapping("/info")
    public Map<String, String> info() {
        return Map.of("service", "dispatch-service", "configMessage", infoMessage);
    }

    // ----- Reports -----

    /** Ingest a report from Issue Reporting (mocked hand-over for Task 1). */
    @PostMapping("/reports")
    public ResponseEntity<Report> createReport(@RequestBody Map<String, String> body) {
        Report report = dispatchService.receiveReport(
                body.get("sourceReportId"),
                body.get("title"),
                body.get("description"),
                body.get("location"));
        return ResponseEntity.status(HttpStatus.CREATED).body(report);
    }

    @GetMapping("/reports")
    public List<Report> allReports() {
        return dispatchService.findAll();
    }

    @GetMapping("/reports/open")
    public List<Report> openReports() {
        return dispatchService.findAllOpenReports();
    }

    @GetMapping("/reports/{id}")
    public Report getReport(@PathVariable String id) {
        return dispatchService.findById(id);
    }

    @GetMapping("/reports/status/{status}")
    public List<Report> byStatus(@PathVariable String status) {
        return dispatchService.findByStatus(ReportStatus.valueOf(status.toUpperCase()));
    }

    // ----- Lifecycle operations (DispatchService) -----

    @PostMapping("/reports/{id}/priority")
    public Report prioritize(@PathVariable String id, @RequestBody Map<String, String> body) {
        return dispatchService.prioritizeReport(id, Priority.valueOf(body.get("priority").toUpperCase()));
    }

    @PostMapping("/reports/{id}/assign")
    public Report assign(@PathVariable String id, @RequestBody Map<String, String> body) {
        String assignedTo = body.get("assignedTo");
        LocalDateTime dueDate = body.containsKey("dueDate") && body.get("dueDate") != null
                && !body.get("dueDate").isBlank()
                ? LocalDateTime.parse(body.get("dueDate"))
                : LocalDateTime.now().plusDays(7);
        return dispatchService.assignReport(id, assignedTo, dueDate);
    }

    @PostMapping("/reports/{id}/status")
    public Report updateStatus(@PathVariable String id, @RequestBody Map<String, String> body) {
        String changedBy = body.getOrDefault("changedBy", "AUTHORITY");
        return dispatchService.updateReportStatus(id,
                ReportStatus.valueOf(body.get("status").toUpperCase()), changedBy);
    }

    @PostMapping("/reports/{id}/resolve")
    public Report resolve(@PathVariable String id, @RequestBody(required = false) Map<String, String> body) {
        String changedBy = body != null ? body.getOrDefault("changedBy", "AUTHORITY") : "AUTHORITY";
        return dispatchService.resolveReport(id, changedBy);
    }

    @PostMapping("/reports/{id}/reject")
    public Report reject(@PathVariable String id, @RequestBody(required = false) Map<String, String> body) {
        String changedBy = body != null ? body.getOrDefault("changedBy", "AUTHORITY") : "AUTHORITY";
        return dispatchService.rejectReport(id, changedBy);
    }

    @GetMapping("/reports/{id}/assignments")
    public List<WorkAssignment> assignments(@PathVariable String id) {
        return dispatchService.findAssignments(id);
    }

    @GetMapping("/reports/{id}/history")
    public List<StatusHistory> history(@PathVariable String id) {
        return dispatchService.findHistory(id);
    }

    // ----- Mocked upstream dependency (User_Service) -----

    /**
     * Technician list fetched from User_Service (GET /api/users/role/TECHNICIAN) via
     * Eureka discovery, guarded by a Resilience4j circuit breaker. If User_Service is
     * down or the circuit is open, a fallback list is returned instead.
     */
    @GetMapping("/technicians")
    public List<Map<String, Object>> technicians() {
        return userServiceClient.getTechnicians();
    }

    // ----- Error handling -----

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<Map<String, String>> handleErrors(RuntimeException ex) {
        HttpStatus status = ex instanceof IllegalStateException
                ? HttpStatus.CONFLICT : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(Map.of("error", ex.getMessage()));
    }
}
