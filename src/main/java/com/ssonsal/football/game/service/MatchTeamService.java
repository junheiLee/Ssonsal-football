package com.ssonsal.football.game.service;

import com.ssonsal.football.game.dto.request.ApprovalTeamRequestDto;
import com.ssonsal.football.game.dto.response.GameResultResponseDto;
import com.ssonsal.football.game.entity.Game;

public interface MatchTeamService {

    Long approvalAwayTeam(Long userId, Long gameId, ApprovalTeamRequestDto approvalAwayTeamDto);

    GameResultResponseDto enterAwayTeamResult(Game game, Integer resultScore);

    GameResultResponseDto enterHomeTeamResult(Game game, Integer resultScore);
}
