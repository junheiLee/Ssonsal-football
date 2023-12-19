package com.ssonsal.football.review.repository;

import com.ssonsal.football.review.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByReportCode(int deleteCode);
}
