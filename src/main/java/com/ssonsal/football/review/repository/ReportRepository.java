package com.ssonsal.football.review.repository;

import com.ssonsal.football.review.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface ReportRepository extends JpaRepository<Report, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE Report r SET r.reportCode = :reportCode WHERE r.id = :reportId")
    void updateReportCode(@Param("reportId") Long reportId, @Param("reportCode") Integer reportCode);
}
