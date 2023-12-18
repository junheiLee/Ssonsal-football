package com.ssonsal.football.game.repository;

import com.ssonsal.football.game.entity.Sub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubRepository extends JpaRepository<Sub, Long> {

    List<Sub> findByUser_Id(Long userId);

    List<Sub> findByGameIdAndTeamId(Long teamId, Long gameId);

    int countByTeamIdAndGameId(Long teamId, Long gameId);

}