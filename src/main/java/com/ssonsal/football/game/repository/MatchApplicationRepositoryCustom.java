package com.ssonsal.football.game.repository;

import com.ssonsal.football.game.dto.response.MatchTeamResponseDto;

public interface MatchApplicationRepositoryCustom {

    MatchTeamResponseDto searchMatchTeamDto(Long matchTeamId);
}
