package com.ssonsal.football.game.controller;

import com.ssonsal.football.game.dto.request.AcceptTeamRequestDto;
import com.ssonsal.football.game.dto.response.MatchTeamResponseDto;
import com.ssonsal.football.game.service.MatchTeamService;
import com.ssonsal.football.global.account.Account;
import com.ssonsal.football.global.account.CurrentUser;
import com.ssonsal.football.global.util.formatter.DataResponseBodyFormatter;
import com.ssonsal.football.global.util.formatter.ResponseBodyFormatter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ssonsal.football.game.util.GameConstant.CONFIRMED_GAME_ID;
import static com.ssonsal.football.game.util.GameConstant.MATCH_TEAM_INFO;
import static com.ssonsal.football.global.util.SuccessCode.SUCCESS;
import static com.ssonsal.football.global.util.transfer.Transfer.longIdToMap;
import static com.ssonsal.football.global.util.transfer.Transfer.toMap;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/match-teams")
@Tag(name = "MatchTeam", description = "MatchTeam API")
public class MatchTeamController {

    private final MatchTeamService matchTeamService;

    /**
     * 게임에의 확정된 팀의 신청 및 팀 정보를 반환하는 api
     *
     * @param matchApplicationId 해당 게임에 확정된 신청 아이디
     * @return 게임에 참여하는 팀의 신청 및 팀 정보와 요청자의 기본 정보
     */
    @GetMapping("/{matchApplicationId}")
    public ResponseEntity<ResponseBodyFormatter> readMatchTeamInfo(@PathVariable Long matchApplicationId) {

        MatchTeamResponseDto matchTeamInfo = matchTeamService.findMatchTeamInfo(matchApplicationId);
        return DataResponseBodyFormatter.put(SUCCESS, toMap(MATCH_TEAM_INFO, matchTeamInfo));
    }

    /**
     * 신청한 팀 중 한 팀을 대상으로 승인하는 기능
     *
     * @param acceptTeamDto 승인 대상 팀 정보
     * @return 성공 코드와 해당 게임 아이디를 ResponseBody에 담아 반환
     */
    @PostMapping
    public ResponseEntity<ResponseBodyFormatter> acceptAwayTeam(@RequestBody AcceptTeamRequestDto acceptTeamDto,
                                                                @CurrentUser Account account) {

        Long loginUserId = account.getId();
        Long confirmedGameId = matchTeamService.acceptAwayTeam(loginUserId, acceptTeamDto);

        return DataResponseBodyFormatter.put(SUCCESS, longIdToMap(CONFIRMED_GAME_ID, confirmedGameId));
    }


}
