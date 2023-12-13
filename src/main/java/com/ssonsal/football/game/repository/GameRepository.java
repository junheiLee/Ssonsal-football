package com.ssonsal.football.game.repository;

import com.ssonsal.football.game.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
}
