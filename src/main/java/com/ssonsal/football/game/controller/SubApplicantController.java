package com.ssonsal.football.game.controller;

import com.ssonsal.football.game.dto.request.SubApplyListDto;
import com.ssonsal.football.game.service.SubApplicantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/games")
public class SubApplicantController {

    private final SubApplicantService subApplicantService;

    /**
     * 팀별 용병 신청 리스트 현황
     *
     * @param logInUser 로그인 한 유저 아이디
     * @param gameId    게임아이디
     * @param teamId    팀아이디
     * @return
     */
    @GetMapping("/{gameId}/teams/{teamId}/sub-applicants")
    public List<SubApplyListDto> getSubRecordsByGameAndTeamId(@RequestBody Map<String, Long> logInUser, @PathVariable Long gameId, @PathVariable Long teamId) {
        return subApplicantService.getSubRecordsByGameAndTeamId(logInUser.get("userId"), gameId, teamId);
    }

    @PostMapping("/{gameId}/teams/{teamId}/sub-applicants") // 용병 신청
    public String SubApply(@RequestBody Map<String, Long> logInUser, @PathVariable Long gameId, @PathVariable Long teamId) {
        return subApplicantService.subApplicant(logInUser.get("userId"), gameId, teamId);
    }

    @PostMapping("/{gameId}/teams/{teamId}/sub-applicants/{userId}")//용병 거절
    public String subReject(@RequestBody Map<String, Long> logInUser, @PathVariable Long gameId, @PathVariable Long teamId) {
        return subApplicantService.subReject(logInUser.get("userId"), gameId, teamId);
    }

}
