package com.ssonsal.football.game.service;

import com.ssonsal.football.game.dto.request.MatchTeamRequestDto;

public interface MatchTeamService {

    Long applyForGameAsAway(Long gameId, Long userId, MatchTeamRequestDto awayteamRequestDto);
}
