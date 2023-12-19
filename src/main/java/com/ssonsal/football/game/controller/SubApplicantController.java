package com.ssonsal.football.game.controller;

import com.ssonsal.football.game.dto.response.SubApplicantsResponseDto;
import com.ssonsal.football.game.service.SubApplicantService;
import com.ssonsal.football.global.config.security.JwtTokenProvider;
import com.ssonsal.football.global.util.SuccessCode;
import com.ssonsal.football.global.util.formatter.DataResponseBodyFormatter;
import com.ssonsal.football.global.util.formatter.ResponseBodyFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.ssonsal.football.game.util.GameConstant.*;
import static com.ssonsal.football.game.util.Transfer.longIdToMap;
import static com.ssonsal.football.game.util.Transfer.toMapIncludeUserInfo;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/match-applications")
public class SubApplicantController {

    private final SubApplicantService subApplicantService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 팀별 용병 신청 리스트 현황
     *
     * @param matchApplicationId 해당 팀 신청 식별자
     * @return 해당 게임의 해당 팀의 용병 신청 목록과 요청한 회원의 기본 정보
     */
    @GetMapping("{matchApplicationId}/sub-applicants")
    public ResponseEntity<ResponseBodyFormatter> subApplicantsByTeamAndGame(@PathVariable Long matchApplicationId, HttpServletRequest request) {

        Long userId = jwtTokenProvider.getUserId(request.getHeader("ssonToken"));
        Long userTeamId = jwtTokenProvider.getTeamId(request.getHeader("ssonToken"));

        List<SubApplicantsResponseDto> subApplicants
                = subApplicantService.getSubApplicantsByMatchApplication(matchApplicationId);

        return DataResponseBodyFormatter.put(SuccessCode.SUCCESS,
                toMapIncludeUserInfo(userId, userTeamId, SUB_APPLICANTS, subApplicants));
    }

    /**
     * 로그인한 사용자가 매치 팀에 용병 신청하는 api
     *
     * @param matchApplicationId 해당 매치 팀 식별자
     * @return 생성된 용병 신청 식별자
     */
    @PostMapping("/{matchApplicationId}/sub-applicants")
    public ResponseEntity<ResponseBodyFormatter> applyGameAsSub(@PathVariable Long matchApplicationId, HttpServletRequest request) {

        Long loginUserId = jwtTokenProvider.getUserId(request.getHeader("ssonToken"));

        Long subApplicantId = subApplicantService.applySubApplicant(loginUserId, matchApplicationId);
        return DataResponseBodyFormatter.put(SuccessCode.SUCCESS, longIdToMap(SUB_APPLICANT_ID, subApplicantId));
    }

    /**
     * 용병 신청을 마감하는 api
     *
     * @param matchApplicationId 해당 매치 팀 식별자
     * @return 마감된 매치 팀 식별자
     */
    @DeleteMapping("/{matchApplicationId}/sub-applicants")
    public ResponseEntity<ResponseBodyFormatter> closeSubApplicant(@PathVariable Long matchApplicationId, HttpServletRequest request) {

        Long loginUserId = jwtTokenProvider.getUserId(request.getHeader("ssonToken"));

        Long closedMatchApplicationId = subApplicantService.closeSubApplicant(loginUserId, matchApplicationId);

        return DataResponseBodyFormatter.put(SuccessCode.SUCCESS,
                longIdToMap(CLOSED_MATCH_APPLICATION_ID, closedMatchApplicationId));
    }

    /**
     * 용병 신청을 거절하는 api
     *
     * @param subApplicantId 거절할 용병 신청 식별자
     * @return 거절된 용병 신청을 한 회원 아이디
     */
    @DeleteMapping("/{matchApplicationId}/sub-applicants/{subApplicantId}")
    public ResponseEntity<ResponseBodyFormatter> rejectSubApplicant(@PathVariable Long matchApplicationId,
                                                                    @PathVariable Long subApplicantId, HttpServletRequest request) {

        Long loginUserId = jwtTokenProvider.getUserId(request.getHeader("ssonToken"));
        Long loginUserTeamId = jwtTokenProvider.getTeamId(request.getHeader("ssonToken"));

        Long rejectSubUserId = subApplicantService.rejectSubApplicant(loginUserId, loginUserTeamId, subApplicantId);
        return DataResponseBodyFormatter.put(SuccessCode.SUCCESS, longIdToMap(REJECTED_SUB_USER_ID, rejectSubUserId));
    }

}
