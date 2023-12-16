package com.ssonsal.football.team.repository;

import com.ssonsal.football.team.entity.TeamRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRecordRepository extends JpaRepository<TeamRecord, Long>, TeamRecordRepositoryCustom {
}
