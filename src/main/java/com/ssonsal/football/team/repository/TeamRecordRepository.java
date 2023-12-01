package com.ssonsal.football.team.repository;

import com.ssonsal.football.team.entity.TeamRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeamRecordRepository extends JpaRepository<TeamRecord,Long> {

    @Query("SELECT COUNT(t) + 1 FROM TeamRecord t WHERE t.point > (SELECT tr.point FROM TeamRecord tr WHERE tr.team.id = :teamId)")
    Integer findRankById(@Param("teamId") Long teamId);

}
