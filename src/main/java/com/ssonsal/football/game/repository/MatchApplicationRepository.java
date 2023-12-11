package com.ssonsal.football.game.repository;

import com.ssonsal.football.game.dto.response.MatchApplicationsResponseDto;
import com.ssonsal.football.game.entity.MatchApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface MatchApplicationRepository extends JpaRepository<MatchApplication, Long> {

    Optional<MatchApplication> findByGameIdAndTeamId(Long gameId, Long teamId);

    List<MatchApplicationsResponseDto> findByGameIdAndApplicationStatusIs(Long gameId, String applicationStatus);
}
