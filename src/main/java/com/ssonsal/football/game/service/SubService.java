package com.ssonsal.football.game.service;


import com.ssonsal.football.game.dto.request.ApprovalSubRequestDto;
import com.ssonsal.football.game.dto.response.SubsResponseDto;

import java.util.List;

public interface SubService {

    // 팀에 소속된 용병 (각 게임별)
    List<SubsResponseDto> getTeamSubList(Long matchApplicationId);

    // 용병신청에 대한 승낙
    Long acceptSub(Long loginUserId, Long matchApplicationId, ApprovalSubRequestDto approvalSubDto);


}
