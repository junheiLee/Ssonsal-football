package com.ssonsal.football.game.repository;

import com.ssonsal.football.game.entity.SubApplicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubApplicantRepository extends JpaRepository<SubApplicant, Long> {

    @Query("SELECT sa FROM SubApplicant sa WHERE match_team_id = :teamId")
    List<SubApplicant> findByMatchTeamId(@Param("teamId") Long teamId);

}