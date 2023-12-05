package com.ssonsal.football.game.service;

import com.ssonsal.football.game.dto.request.approvalAwayTeamRequestDto;

public interface MatchTeamService {

    Long approvalAwayTeam(Long userId, Long gameId, approvalAwayTeamRequestDto approvalAwayTeamDto);
}
