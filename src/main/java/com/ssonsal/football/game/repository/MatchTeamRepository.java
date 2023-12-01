package com.ssonsal.football.game.repository;

import com.ssonsal.football.game.entity.MatchTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchTeamRepository extends JpaRepository<MatchTeam, Long> {
}
