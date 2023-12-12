package com.ssonsal.football.game.service;

import com.ssonsal.football.game.dto.request.MatchApplicationRequestDto;
import com.ssonsal.football.game.dto.response.MatchApplicationsResponseDto;

import java.util.List;

public interface MatchApplicantService {

    /**
     * 대기 상태인 해당 게임의 신청 팀 복록을 반환
     *
     * @param gameId 신청팀 목록을 불러오고자 하는 게임의 식별자
     * @return 대기 상태인 신청 팀 목록
     */
    List<MatchApplicationsResponseDto> findWaitingApplications(Long gameId);

    /**
     * 회원이 팀에 소속되어있고,
     * 등록팀의 팀원이 아니고, 이미 신청한 팀의 팀원이 아닌 경우
     * 해당 게임에 대한 회원의 팀 신청을 추가한다.
     *
     * @param loginUserId        신청하고자 하는 회원의 식별자
     * @param gameId             신청하고자 하는 해당 게임 식별자
     * @param applicationTeamDto 신청 시 기입한 정보
     * @return 신청서 식별자
     */
    Long applyToMatchAsAway(Long loginUserId, Long gameId, MatchApplicationRequestDto applicationTeamDto);

    /**
     * 회원이 해당 게임의 작성자인 경우,
     * 해당 신청의 신청 상태를 거절으로 변경한다.
     *
     * @param loginUserId        거절하고자 하는 회원의 식별자
     * @param gameId             해당 게임 식별자
     * @param matchApplicationId 거절하고자 하는 신청의 식별자
     * @return 거절된 신청의 식별자
     */
    Long rejectMatchApplication(Long loginUserId, Long gameId, Long matchApplicationId);
}
