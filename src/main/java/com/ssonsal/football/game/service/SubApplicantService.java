package com.ssonsal.football.game.service;

import com.ssonsal.football.game.dto.response.SubApplicantsResponseDto;

import java.util.List;

public interface SubApplicantService {

    // 팀별 용병 신청 현황
    List<SubApplicantsResponseDto> getSubApplicantsByGameAndTeam(Long teamId, Long gameId);

    // 용병 신청
    Long applySubApplicant(Long userId, Long teamId, Long gameId);

    // 용병 신청에 대한 거절
    Long rejectSubApplicant(Long userId, Long userTeamId, Long targetSubApplicantId);

}
