package com.example.Dispatch_Service.repository;

import com.example.Dispatch_Service.model.WorkAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkAssignmentRepository extends JpaRepository<WorkAssignment, String> {

    List<WorkAssignment> findByReportId(String reportId);

    List<WorkAssignment> findByAssignedTo(String assignedTo);
}
