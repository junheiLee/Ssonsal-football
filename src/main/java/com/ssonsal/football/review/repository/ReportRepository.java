package com.ssonsal.football.review.repository;

import com.ssonsal.football.review.etity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}