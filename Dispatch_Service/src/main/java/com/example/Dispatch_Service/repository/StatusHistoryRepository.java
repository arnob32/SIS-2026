package com.example.Dispatch_Service.repository;

import com.example.Dispatch_Service.model.StatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusHistoryRepository extends JpaRepository<StatusHistory, String> {

    List<StatusHistory> findByReportId(String reportId);
}
