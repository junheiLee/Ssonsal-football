package com.ssonsal.football.game.service;

import com.ssonsal.football.game.dto.request.MatchApplicationRequestDto;

public interface MatchApplicantService {

    Long applyForGameAsAway(Long gameId, Long userId, MatchApplicationRequestDto applicationTeamDto);
}
