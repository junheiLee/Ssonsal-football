package com.ssonsal.football.game.repository;

import com.ssonsal.football.game.entity.Game;
import com.ssonsal.football.game.entity.MatchApplication;
import com.ssonsal.football.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MatchApplicationRepository extends JpaRepository<MatchApplication, Long> {

    MatchApplication findByGameAndTeam(Game game, Team team);

    Optional<MatchApplication> findByGameIdAndTeamId(Long gameId, Long teamId);
}