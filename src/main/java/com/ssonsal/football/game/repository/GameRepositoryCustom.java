package com.ssonsal.football.game.repository;

import com.ssonsal.football.game.dto.response.GameListResponseDto;

import java.util.List;

public interface GameRepositoryCustom {

    List<GameListResponseDto> searchAllGameForSub();

    List<GameListResponseDto> searchMyGameAsSub(Long userId);

    List<GameListResponseDto> searchOurGameAsTeam(Long teamId);
}
