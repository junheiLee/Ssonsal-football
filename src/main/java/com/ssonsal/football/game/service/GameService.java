package com.ssonsal.football.game.service;


import com.ssonsal.football.game.dto.request.GameRequestDto;
import com.ssonsal.football.game.dto.request.MatchApplicationRequestDto;

import java.text.ParseException;

public interface GameService {

    Long createGame(Long userId, GameRequestDto gameRequestDto, MatchApplicationRequestDto homeTeamDto) throws ParseException;
}
