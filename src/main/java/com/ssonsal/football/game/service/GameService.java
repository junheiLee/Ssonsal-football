package com.ssonsal.football.game.service;


import com.ssonsal.football.game.dto.request.GameRequestDto;
import com.ssonsal.football.game.dto.request.GameResultRequestDto;
import com.ssonsal.football.game.dto.request.MatchApplicationRequestDto;
import com.ssonsal.football.game.dto.response.GameDetailResponseDto;
import com.ssonsal.football.game.dto.response.GameListResponseDto;
import com.ssonsal.football.game.dto.response.GameResultResponseDto;

import java.util.List;

public interface GameService {

    Long createGame(Long userId, GameRequestDto gameRequestDto, MatchApplicationRequestDto homeTeamDto);

    GameDetailResponseDto findById(Long gameId);

    Long updateGame(Long userId, Long gameId,
                    GameRequestDto updateGameRequestDto, MatchApplicationRequestDto updateHomeTeamDto);

    GameResultResponseDto enterResult(Long userId, Long gameId, GameResultRequestDto gameResultDto);

    List<GameListResponseDto> findAllGamesForTeam();

    List<GameListResponseDto> findAllGamesForSub();

    List<GameListResponseDto> findMyGamesAsSub(Long userId);

    List<GameListResponseDto> findOurGamesAsTeam(Long teamId);

}
