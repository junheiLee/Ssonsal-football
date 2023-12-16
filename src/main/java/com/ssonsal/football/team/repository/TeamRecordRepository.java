package com.ssonsal.football.team.repository;

import com.ssonsal.football.team.entity.TeamRecord;
import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD
=======
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
>>>>>>> f866c1803d7abb343151d947d54568fc79e8b5eb

public interface TeamRecordRepository extends JpaRepository<TeamRecord, Long>, TeamRecordRepositoryCustom {
}
