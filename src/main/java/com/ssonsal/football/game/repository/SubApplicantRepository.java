package com.ssonsal.football.game.repository;

import com.ssonsal.football.game.entity.SubApplicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SubApplicantRepository extends JpaRepository<SubApplicant, Long> {

    @Query("SELECT sa FROM SubApplicant sa WHERE match_team_id = :teamId")
    List<SubApplicant> findByMatchTeamId(@Param("teamId") Long teamId);

    @Transactional
    @Modifying
    @Query("UPDATE MatchApplication mt SET mt.subCount = mt.subCount - 1 WHERE mt.game.id = :gameId AND mt.team.id = :teamId")
    void decreaseSubCount(@Param("gameId") Long gameId, @Param("teamId") Long teamId);



}