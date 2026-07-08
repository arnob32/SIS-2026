package com.example.Dispatch_Service.repository;

import com.example.Dispatch_Service.model.Report;
import com.example.Dispatch_Service.model.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, String> {

    List<Report> findByStatus(ReportStatus status);

    /** Open reports are those not yet in a terminal state. */
    List<Report> findByStatusNotIn(Collection<ReportStatus> statuses);
}
