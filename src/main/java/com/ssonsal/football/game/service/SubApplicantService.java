package com.ssonsal.football.game.service;

import com.ssonsal.football.game.dto.response.SubApplicantsResponseDto;
import com.ssonsal.football.team.entity.Team;

import java.util.List;

public interface SubApplicantService {


    /**
     * 팀 신청에 용병을 신청하는 기능
     *
     * @param userId             신청을 하고자 하는 로그인한 회원 식별자
     * @param matchApplicationId 해당 팀 신청의 식별자
     * @return 생성된 용병 신청 식별자
     */
    Long applySubApplicant(Long userId, Long matchApplicationId);

    /**
     * 용병 신청을 거절하는 기능
     *
     * @param userTeam             신청을 거절하고자 하는 회원의 소속 팀
     * @param targetSubApplicantId 거절하고자 하는 해당 용병 신청 아이디
     * @return 거절된 용병의 회원 아이디
     */
    Long rejectSubApplicant(Team userTeam, Long targetSubApplicantId);

    /**
     * 해당 팀 신청의 용병 구하는 수를 0으로 만들어 용병 모집을 마감하는 기능
     *
     * @param loginUserId        마감하고자 하는 회원의 아이디
     * @param matchApplicationId 해당 팀 신청
     * @return 마감된 매치 팀 식별자
     */
    Long closeSubApplicant(Long loginUserId, Long matchApplicationId);

    /**
     * 해당 팀 신청의 대기중인 용병 신청 목록 반환 기능
     *
     * @param matchApplicationId 해당 팀 신청의 식별자
     * @return 해당 팀 신청의 대기중인 용병 신청 목록
     */
    List<SubApplicantsResponseDto> getSubApplicantsByMatchApplication(Long matchApplicationId);

}
