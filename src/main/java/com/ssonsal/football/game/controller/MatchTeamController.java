package com.ssonsal.football.game.controller;

import com.ssonsal.football.game.dto.request.ApprovalTeamRequestDto;
import com.ssonsal.football.game.service.MatchTeamService;
import com.ssonsal.football.global.util.SuccessCode;
import com.ssonsal.football.global.util.formatter.DataResponseBodyFormatter;
import com.ssonsal.football.global.util.formatter.ResponseBodyFormatter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/games")
@Tag(name = "MatchTeam", description = "MatchTeam API")
public class MatchTeamController {

    private final MatchTeamService matchTeamService;

    /**
     * 신청한 팀 중 한 팀을 대상으로 승인하는 기능
     * <p>
     * 게임을 등록한 팀의 팀원이 아닌 유저가 접근하는 경우 예외 발생
     * 이미 확정된 상태의 게임이 대상인 경우 예외 발생
     * 존재하지 않는 게임일 경우 예외 발생
     *
     * @param gameId              url에서 가져오는 해당 게임의 식별자
     * @param approvalAwayTeamDto 승인 대상 팀 정보
     * @return 성공 코드와 해당 게임 아이디를 ResponseBody에 담아 반환
     */
    @PostMapping("/{gameId}/match-teams")
    public ResponseEntity<ResponseBodyFormatter> approvalAwayTeam(
            @PathVariable Long gameId, @RequestBody ApprovalTeamRequestDto approvalAwayTeamDto) {

        Long userId = 6L;
        Long confirmedGameId = matchTeamService.approvalAwayTeam(userId, gameId, approvalAwayTeamDto);

        return DataResponseBodyFormatter.put(SuccessCode.SUCCESS, dataToMap("gameId", confirmedGameId));
    }

    private Map<String, Long> dataToMap(String key, Long value) {

        Map<String, Long> dataDto = new HashMap<>();
        dataDto.put(key, value);
        return dataDto;
    }
}
