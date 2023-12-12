package com.ssonsal.football.game.controller;

import com.ssonsal.football.game.dto.response.SubApplicantsResponseDto;
import com.ssonsal.football.game.service.SubApplicantService;
import com.ssonsal.football.global.util.SuccessCode;
import com.ssonsal.football.global.util.formatter.DataResponseBodyFormatter;
import com.ssonsal.football.global.util.formatter.ResponseBodyFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.ssonsal.football.game.util.GameConstant.*;
import static com.ssonsal.football.game.util.Transfer.longIdToMap;
import static com.ssonsal.football.game.util.Transfer.toMapIncludeUserInfo;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/games")
public class SubApplicantController {

    private final SubApplicantService subApplicantService;

    /**
     * 팀별 용병 신청 리스트 현황
     *
     * @param gameId 게임아이디
     * @param teamId 팀아이디
     * @return 해당 게임의 해당 팀의 용병 신청 목록과 요청한 회원의 기본 정보
     */
    @GetMapping("/{gameId}/teams/{teamId}/sub-applicants")
    public ResponseEntity<ResponseBodyFormatter> subApplicantsByTeamAndGame(@PathVariable Long gameId,
                                                                            @PathVariable Long teamId) {

        Long userId = 11L;
        Long userTeamId = null;
        List<SubApplicantsResponseDto> subApplicants = subApplicantService.getSubApplicantsByGameAndTeam(teamId, gameId);

        return DataResponseBodyFormatter.put(SuccessCode.SUCCESS,
                toMapIncludeUserInfo(userId, userTeamId, SUB_APPLICANTS, subApplicants));
    }

    /**
     * 매치 팀에 용병 신청하는 api
     *
     * @param targetUser RequestBody 에 담겨 있는 용병 신청하는 userId
     * @param gameId     매치 팀에 해당하는 게임 식별자
     * @param teamId     매치 팀에 해당하는 팀 식별자
     * @return 생성된 용병 신청 식별자
     */
    @PostMapping("/{gameId}/teams/{teamId}/sub-applicants")
    public ResponseEntity<ResponseBodyFormatter> applyGameAsSub(@RequestBody Map<String, Long> targetUser,
                                                                @PathVariable Long gameId,
                                                                @PathVariable Long teamId) {

        Long subApplicantId = subApplicantService.applySubApplicant(targetUser.get("userId"), teamId, gameId);
        return DataResponseBodyFormatter.put(SuccessCode.SUCCESS, longIdToMap(SUB_APPLICANT_ID, subApplicantId));
    }

    /**
     * 용병 신청을 거절하는 api
     *
     * @param subApplicantId 거절할 용병 신청 식별자
     * @return 거절된 용병 신청을 한 회원 아이디
     */
    @DeleteMapping("/{gameId}/teams/{teamId}/sub-applicants/{subApplicantId}")
    public ResponseEntity<ResponseBodyFormatter> rejectSubApplicant(@PathVariable Long subApplicantId) {
        Long userId = 1L;
        Long userTeamId = 1L;

        Long rejectSubUserId = subApplicantService.rejectSubApplicant(userId, userTeamId, subApplicantId);
        return DataResponseBodyFormatter.put(SuccessCode.SUCCESS, longIdToMap(REJECTED_SUB_USER_ID, rejectSubUserId));
    }

}
