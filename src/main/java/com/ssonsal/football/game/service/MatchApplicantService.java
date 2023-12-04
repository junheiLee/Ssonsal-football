package com.ssonsal.football.game.service;

import com.ssonsal.football.game.dto.request.MatchApplicantRequestDto;

public interface MatchApplicantService {

    Long applyForGameAsAway(Long gameId, Long userId, MatchApplicantRequestDto awayteamDto);
}
