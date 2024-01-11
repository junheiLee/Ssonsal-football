package com.ssonsal.football.team.repository;

import com.ssonsal.football.team.entity.TeamRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TeamRecordRepository extends JpaRepository<TeamRecord, Long>, TeamRecordRepositoryCustom {
    List<TeamRecord> findByModifiedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}
