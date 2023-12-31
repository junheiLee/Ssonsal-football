package com.ssonsal.football.game.controller;

import com.ssonsal.football.game.dto.request.MatchApplicationRequestDto;
import com.ssonsal.football.game.dto.response.MatchApplicationsResponseDto;
import com.ssonsal.football.game.service.MatchApplicantService;
import com.ssonsal.football.global.util.formatter.DataResponseBodyFormatter;
import com.ssonsal.football.global.util.formatter.ResponseBodyFormatter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ssonsal.football.game.util.GameConstant.*;
import static com.ssonsal.football.game.util.Transfer.longIdToMap;
import static com.ssonsal.football.game.util.Transfer.toMapIncludeUserInfo;
import static com.ssonsal.football.global.util.SuccessCode.SUCCESS;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/games")
@Tag(name = "MatchApplicant", description = "MatchApplicant API")
public class MatchApplicantController {

    private final MatchApplicantService matchApplicantService;

    /**
     * 해당 게임에 신청한 대기 중인 신청 목록을 반환하는 api
     *
     * @param gameId 해당 게임 식별자
     * @return 해당 게임에서 대기중인 신청 목록과 요청한 회원의 정보
     */
    @GetMapping("/{gameId}/match-applications")
    public ResponseEntity<ResponseBodyFormatter> matchApplications(@PathVariable Long gameId) {

        Long loginUserId = 2L;
        Long loginUserTeamId = null;
        List<MatchApplicationsResponseDto> matchApplications = matchApplicantService.findWaitingApplications(gameId);
        return DataResponseBodyFormatter
                .put(SUCCESS, toMapIncludeUserInfo(loginUserId, loginUserTeamId, MATCH_APPLICATIONS, matchApplications));
    }

    /**
     * 상대 팀으로 게임 신청 시, 호출되는 api
     * <p>
     * 해당 게임의 등록팀 팀원일 경우 예외 발생
     * 해당 게임에 이미 신청한 팀의 팀원인 경우 예외 발생
     * 유저가 팀이 소속되지 않은 경우 예외 발생
     *
     * @param applicationTeamDto 게임 신청에 필요한 정보
     * @param gameId             url에서 가져오는 해당 게임의 식별자
     * @return 성공 코드와 생성된 매치팀 아이디를 ResponseBody에 담아 반환
     */
    @PostMapping("/{gameId}/match-applications")
    public ResponseEntity<ResponseBodyFormatter> applyToGameAsAway(@RequestBody MatchApplicationRequestDto applicationTeamDto,
                                                                   @PathVariable Long gameId) {

        Long loginUserId = 7L;
        Long matchApplicantId = matchApplicantService.applyToMatchAsAway(loginUserId, gameId, applicationTeamDto);

        return DataResponseBodyFormatter
                .put(SUCCESS, longIdToMap(MATCH_APPLICATION_ID, matchApplicantId));
    }

    /**
     * 해당 게임에 대한 신청을 거절하는 api
     *
     * @param matchApplicationId 해당 신청 식별자
     * @param gameId             해당 게임
     * @return 거절된 신청 아이디 반환
     */
    @DeleteMapping("/{gameId}/match-applications/{matchApplicationId}")
    public ResponseEntity<ResponseBodyFormatter> rejectApplicationAsAway(@PathVariable Long matchApplicationId,
                                                                         @PathVariable Long gameId) {

        Long loginUserId = 3L;
        Long rejectedMatchApplicationId
                = matchApplicantService.rejectMatchApplication(loginUserId, gameId, matchApplicationId);

        return DataResponseBodyFormatter
                .put(SUCCESS, longIdToMap(REJECTED_MATCH_APPLICATION_ID, rejectedMatchApplicationId));
    }


}
