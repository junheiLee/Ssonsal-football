package com.ssonsal.football.game.repository;

import com.ssonsal.football.game.entity.MatchApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchTeamRepository extends JpaRepository<MatchApplication, Long> {
}
