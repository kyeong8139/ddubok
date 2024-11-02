package com.ddubok.api.report.repository;

import com.ddubok.api.report.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.ddubok.api.report.entity.State;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query("SELECT r FROM Report r WHERE r.state = :state ORDER BY r.id DESC")
    Page<Report> findByState(State state, Pageable pageable);

    @Query("SELECT r FROM Report r ORDER BY r.id DESC")
    Page<Report> findAll(Pageable pageable);
}