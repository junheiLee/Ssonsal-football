package com.ssonsal.football.game.service;


import com.ssonsal.football.game.dto.request.SubInTeamDto;

import java.util.List;

public interface SubService {


    // 용병신청에 대한 승낙
    String subAccept(Long userId, Long gameId, Long teamId);


    // 팀에 소속된 용병 (각 게임별)
    List<SubInTeamDto> getTeamSubList(Long gameId, Long teamId);
}
