package com.ssonsal.football.game.service;

import com.ssonsal.football.game.dto.request.ApprovalTeamRequestDto;

public interface MatchTeamService {

    Long approvalAwayTeam(Long userId, Long gameId, ApprovalTeamRequestDto approvalAwayTeamDto);
}
