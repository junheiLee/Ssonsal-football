package com.ssonsal.football.game.controller;

import com.ssonsal.football.game.dto.request.MatchApplicationRequestDto;
import com.ssonsal.football.game.service.MatchApplicantService;
import com.ssonsal.football.game.util.Transfer;
import com.ssonsal.football.global.util.SuccessCode;
import com.ssonsal.football.global.util.formatter.DataResponseBodyFormatter;
import com.ssonsal.football.global.util.formatter.ResponseBodyFormatter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ssonsal.football.game.util.GameConstant.MATCH_APPLICATION_ID;
import static com.ssonsal.football.game.util.GameConstant.REJECTED_MATCH_APPLICATION_ID;
import static com.ssonsal.football.game.util.Transfer.*;
import static com.ssonsal.football.global.util.SuccessCode.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/games")
@Tag(name = "MatchApplicant", description = "MatchApplicant API")
public class MatchApplicantController {

    private final MatchApplicantService matchApplicantService;

    /**
     * 상대 팀으로 게임 신청 시, 호출되는 api
     *
     * @param applicationTeamDto 게임 신청에 필요한 정보
     * @param gameId             url에서 가져오는 해당 게임의 식별자
     * @return 성공 코드와 생성된 매치팀 아이디를 ResponseBody에 담아 반환
     */
    @PostMapping("/{gameId}/match-applications")
    public ResponseEntity<ResponseBodyFormatter> applyToGameAsAway(
            @RequestBody MatchApplicationRequestDto applicationTeamDto,
            @PathVariable Long gameId) {

        Long userId = 7L;
        Long matchApplicantId = matchApplicantService.applyToGameAsAway(gameId, userId, applicationTeamDto);

        return DataResponseBodyFormatter
                .put(SUCCESS, longIdToMap(MATCH_APPLICATION_ID, matchApplicantId));
    }

    @DeleteMapping("/{gameId}/match-applications/{matchApplicationId}")
    public ResponseEntity<ResponseBodyFormatter> rejectApplicationAsAway(@PathVariable Long matchApplicationId,
                                                                         @PathVariable Long gameId) {

        Long userId = 3L;
        Long rejectedMatchApplicationId = matchApplicantService.rejectApplicationAsAway(userId, gameId, matchApplicationId);

        return DataResponseBodyFormatter
                .put(SUCCESS, longIdToMap(REJECTED_MATCH_APPLICATION_ID, rejectedMatchApplicationId));
    }



}
