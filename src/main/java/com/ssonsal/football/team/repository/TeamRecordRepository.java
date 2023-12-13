package com.ssonsal.football.team.repository;

import com.ssonsal.football.team.entity.TeamRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeamRecordRepository extends JpaRepository<TeamRecord, Long>, TeamRecordRepositoryCustom {
}
