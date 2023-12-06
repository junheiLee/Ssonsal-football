package com.ssonsal.football.game.service;

import com.ssonsal.football.game.dto.request.ApprovalTeamRequestDto;
import com.ssonsal.football.game.dto.response.GameResultResponseDto;
import com.ssonsal.football.game.entity.Game;
import com.ssonsal.football.game.util.TeamResult;

public interface MatchTeamService {

    Long approvalAwayTeam(Long userId, Long gameId, ApprovalTeamRequestDto approvalAwayTeamDto);

    GameResultResponseDto enterAwayTeamResult(Game game, TeamResult awayResult);

    GameResultResponseDto enterHomeTeamResult(Game game, TeamResult homeResult);
}
