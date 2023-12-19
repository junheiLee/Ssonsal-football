package com.ssonsal.football.game.controller;

import com.ssonsal.football.game.dto.request.ApprovalTeamRequestDto;
import com.ssonsal.football.game.dto.response.MatchTeamResponseDto;
import com.ssonsal.football.game.service.GameService;
import com.ssonsal.football.game.service.MatchTeamService;
import com.ssonsal.football.global.util.formatter.DataResponseBodyFormatter;
import com.ssonsal.football.global.util.formatter.ResponseBodyFormatter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ssonsal.football.game.util.GameConstant.CONFIRMED_GAME_ID;
import static com.ssonsal.football.game.util.GameConstant.MATCH_TEAM;
import static com.ssonsal.football.global.util.transfer.Transfer.longIdToMap;
import static com.ssonsal.football.global.util.transfer.Transfer.toMapIncludeUserInfo;
import static com.ssonsal.football.global.util.SuccessCode.SUCCESS;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/match-teams")
@Tag(name = "MatchTeam", description = "MatchTeam API")
public class MatchTeamController {

    private final GameService gameService;
    private final MatchTeamService matchTeamService;

    /**
     * 게임에의 신청한 팀의 신청 및 팀 정보를 반환하는 api
     *
     * @param matchApplicationId 해당 게임 신청 아이디
     * @return 게임에 참여하는 팀의 신청 및 팀 정보와 요청자의 기본 정보
     */
    @GetMapping("/{matchApplicationId}")
    public ResponseEntity<ResponseBodyFormatter> readMatchTeam(@PathVariable Long matchApplicationId) {
        Long loginUserId = 1L;
        Long loginUserTeamId = 1L;

        MatchTeamResponseDto matchTeam = matchTeamService.getMatchTeam(matchApplicationId);

        return DataResponseBodyFormatter.put(SUCCESS, toMapIncludeUserInfo(loginUserId, loginUserTeamId, MATCH_TEAM, matchTeam));
    }

    /**
     * 신청한 팀 중 한 팀을 대상으로 승인하는 기능
     *
     * @param approvalTeamDto 승인 대상 팀 정보
     * @return 성공 코드와 해당 게임 아이디를 ResponseBody에 담아 반환
     */
    @PostMapping
    public ResponseEntity<ResponseBodyFormatter> acceptAwayTeam(@RequestBody ApprovalTeamRequestDto approvalTeamDto) {

        Long loginUserId = 6L;
        Long confirmedGameId = matchTeamService.approveAwayTeam(loginUserId, approvalTeamDto);

        return DataResponseBodyFormatter.put(SUCCESS, longIdToMap(CONFIRMED_GAME_ID, confirmedGameId));
    }


}
