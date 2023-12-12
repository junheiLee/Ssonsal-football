package com.ssonsal.football.game.service;

import com.ssonsal.football.game.dto.request.SubApplyListDto;

import java.util.List;

public interface SubApplicantService {

    // 용병 신청
    String subApplicant(Long gameId, Long teamId, Long id);

    // 용병 신청에 대한 거절
    String subReject(Long userId, Long gameId, Long teamId);

    // 팀별 용병 신청 현황
    List<SubApplyListDto> getSubRecordsByGameAndTeamId(Long userId, Long gameId, Long teamId);

}
