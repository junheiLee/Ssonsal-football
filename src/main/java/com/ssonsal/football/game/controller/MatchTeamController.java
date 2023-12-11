package com.ssonsal.football.game.controller;

import com.ssonsal.football.game.dto.request.ApprovalTeamRequestDto;
import com.ssonsal.football.game.dto.request.GameResultRequestDto;
import com.ssonsal.football.game.dto.response.GameResultResponseDto;
import com.ssonsal.football.game.exception.MatchErrorCode;
import com.ssonsal.football.game.service.GameService;
import com.ssonsal.football.game.service.MatchTeamService;
import com.ssonsal.football.game.util.GameSuccessCode;
import com.ssonsal.football.game.util.TeamResult;
import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.global.util.SuccessCode;
import com.ssonsal.football.global.util.formatter.DataResponseBodyFormatter;
import com.ssonsal.football.global.util.formatter.ResponseBodyFormatter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ssonsal.football.game.util.GameConstant.CONFIRMED_GAME_ID;
import static com.ssonsal.football.game.util.Transfer.longIdToMap;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/games")
@Tag(name = "MatchTeam", description = "MatchTeam API")
public class MatchTeamController {

    private final GameService gameService;
    private final MatchTeamService matchTeamService;

    /**
     * 신청한 팀 중 한 팀을 대상으로 승인하는 기능
     * <p>
     * 승-패, 무-무 외의 입력 시, 두 결과를 모두 null로 초기화 후 예외 발생
     * 대기 중이거나 종료된 게임의 경우 예외 발생
     * 상대 팀을 구하지 않은 게임의 경우 예외 발생
     *
     * @param gameId          url에서 가져오는 해당 게임의 식별자
     * @param approvalTeamDto 승인 대상 팀 정보
     * @return 성공 코드와 해당 게임 아이디를 ResponseBody에 담아 반환
     */
    @PostMapping("/{gameId}/match-teams")
    public ResponseEntity<ResponseBodyFormatter> approveAwayTeam(@PathVariable Long gameId,
                                                                 @RequestBody ApprovalTeamRequestDto approvalTeamDto) {

        Long userId = 6L;
        Long confirmedGameId = matchTeamService.approveAwayTeam(userId, gameId, approvalTeamDto);

        return DataResponseBodyFormatter.put(SuccessCode.SUCCESS, longIdToMap(CONFIRMED_GAME_ID, confirmedGameId));
    }

    /**
     * 상대팀을 구한 게임이 확정된 후, 각 팀에 경기 후 결과를 기입하는 기능
     *
     * @param gameId        url에서 가져오는 해당 게임의 식별자
     * @param gameResultDto 확정된 게임에서 기입한 결과와 대상 팀
     * @return 성공 코드와 해당 게임의 각각 두 팀의 result와 두 팀의 result를 더한 값을 ResponseBody에 담아 반환
     */
    @PostMapping("/{gameId}/match-teams/result")
    public ResponseEntity<ResponseBodyFormatter> enterResult(@PathVariable Long gameId,
                                                             @RequestBody GameResultRequestDto gameResultDto) {

        Long userId = 7L;
        GameResultResponseDto gameResult = gameService.enterResult(userId, gameId, gameResultDto);

        return setHttpStatus(gameResult);
    }

    private ResponseEntity<ResponseBodyFormatter> setHttpStatus(GameResultResponseDto gameResult) {

        if (gameResult.getTotalScore() == null) {
            throw new CustomException(MatchErrorCode.IMPOSSIBLE_RESULT, gameResult);
        }
        if (gameResult.getTotalScore() < TeamResult.END.getScore()) {
            return DataResponseBodyFormatter.put(GameSuccessCode.WAIT_FOR_ANOTHER_TEAM, gameResult);
        }

        return DataResponseBodyFormatter.put(SuccessCode.SUCCESS, gameResult);
    }

}
