package com.ssonsal.football.game.repository;

import com.ssonsal.football.game.dto.response.MatchTeamResponseDto;

import java.util.List;

public interface MatchApplicationRepositoryCustom {

    List<MatchTeamResponseDto> searchMatchTeamDto(Long teamId, Long gameId);
}
