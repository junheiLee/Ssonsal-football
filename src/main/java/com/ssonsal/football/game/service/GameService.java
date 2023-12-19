package com.ssonsal.football.game.service;


import com.ssonsal.football.game.dto.request.CreateGameRequestDto;
import com.ssonsal.football.game.dto.request.EnterResultRequestDto;
import com.ssonsal.football.game.dto.response.GameInfoResponseDto;
import com.ssonsal.football.game.dto.response.GameListResponseDto;
import com.ssonsal.football.game.dto.response.GameResultResponseDto;

import java.util.List;

public interface GameService {

    GameInfoResponseDto findGame(Long gameId);

    Long insertGame(Long loginUserId, CreateGameRequestDto createGameDto);

    GameResultResponseDto enterResult(Long loginUserId, Long gameId, EnterResultRequestDto gameResultDto);

    List<GameListResponseDto> findAllGames();

    List<GameListResponseDto> findAllGamesForTeam();

    List<GameListResponseDto> findAllGamesForSub();

    List<GameListResponseDto> findMyGamesAsSub(Long userId);

    List<GameListResponseDto> findOurGamesAsTeam(Long teamId);

}
