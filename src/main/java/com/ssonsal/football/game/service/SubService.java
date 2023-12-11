package com.ssonsal.football.game.service;


import com.ssonsal.football.game.dto.request.SubApplyListDto;
import com.ssonsal.football.game.dto.request.SubRecordDto;
import com.ssonsal.football.game.entity.SubApplicant;

import java.util.List;

public interface SubService {

    // 용병 신청
    String subApplicant(Long gameId, Long teamId, Long id);

    // 용병으로 참여한 기록
    List<SubRecordDto> getSubRecordsByUserId(Long userId);
    // 팀별 용병 신청 현황
    List<SubApplyListDto> getSubRecordsByGameAndTeamId(Long userId, Long gameId, Long teamId);

    // 용병신청에 대한 승낙
    String subAccept(Long userId, Long gameId, Long teamId);
    // 용병 신청에 대한 거절
    String subReject(Long userId, Long gameId, Long teamId);

}
