package com.ssonsal.football.game.repository;

import com.ssonsal.football.game.entity.Game;
import com.ssonsal.football.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long>, GameRepositoryCustom {

    List<Game> findAllByMatchStatus(int matchStatusCode);

    boolean existsByIdAndWriterEquals(Long gameId, User writer);
}
