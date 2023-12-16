package com.ssonsal.football.admin.repository;

import com.ssonsal.football.admin.dto.request.GameDTO;
import com.ssonsal.football.game.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;


public interface GameManagementRepository extends JpaRepository<Game, Long> {

    List<Game> findByScheduleBetween(LocalDateTime startDate, LocalDateTime endDate);

    // 스케줄 날짜가 지나면 deleteCode변환
    @Modifying
    @Query("UPDATE Game g SET g.deleteCode = 2 WHERE g.schedule < :currentDate AND g.deleteCode = 0")
    void updateDeleteCode(@Param("currentDate") LocalDateTime currentDate);
}
