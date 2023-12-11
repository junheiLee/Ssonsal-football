package com.ssonsal.football.game.service;

import com.ssonsal.football.game.dto.request.ApprovalTeamRequestDto;
import com.ssonsal.football.game.dto.response.GameResultResponseDto;
import com.ssonsal.football.game.entity.Game;
import com.ssonsal.football.game.util.TeamResult;

public interface MatchTeamService {

    /**
     * 승인할 팀 정보를 통해 대상 게임의 상대 팀 신청을 승인하는 기능
     *
     * @param userId          해당 기능을 호출한 회원 식별자
     * @param gameId          대상 게임 식별자
     * @param approvalAwayDto 신청을 승인할 팀 정보
     * @return 상대팀이 확정된 게임의 아이디 반환
     */
    Long approveAwayTeam(Long userId, Long gameId, ApprovalTeamRequestDto approvalAwayDto);

    /**
     * 상대팀의 입력일 경우, 등록팀의 결과 기입에 따라 롤백이나 입력 완료.
     *
     * @param game       대상 게임 식별자
     * @param awayResult 기입할 상대팀의 결과
     * @return 현재까지 입력된 양 팀의 결과를 반환
     */
    GameResultResponseDto enterAwayTeamResult(Game game, TeamResult awayResult);

    /**
     * 등록팀의 입력일 경우, 상대팀의 결과 기입에 따라 롤백이나 입력 완료.
     *
     * @param game       대상 게임 식별자
     * @param homeResult 기입할 등록팀의 결과
     * @return 현재까지 입력된 양 팀의 결과를 반환
     */
    GameResultResponseDto enterHomeTeamResult(Game game, TeamResult homeResult);
}
