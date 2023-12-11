package com.ssonsal.football.game.controller;

import com.ssonsal.football.game.dto.request.SubApplyListDto;
import com.ssonsal.football.game.dto.request.SubRecordDto;
import com.ssonsal.football.game.entity.SubApplicant;
import com.ssonsal.football.game.service.SubService;
import com.ssonsal.football.user.entity.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/games")
@Tag(name = "Sub", description = "Sub API")
public class SubRestController {
    @Autowired
    private SubService subService;

    @GetMapping("/subs/{userId}") // 유저가 참여한 게임 목록
    public List<SubRecordDto> getSubsByUserId(@PathVariable Long userId) {
        return subService.getSubRecordsByUserId(userId);
    }

    @GetMapping("/{gameId}/teams/{teamId}/sub-applicants") // 팀별 용병신청리스트 현황
    public List<SubApplyListDto> getSubRecordsByGameAndTeamId(@RequestBody Map<String, Long> logInUser, @PathVariable Long gameId, @PathVariable Long teamId){
        return subService.getSubRecordsByGameAndTeamId(logInUser.get("userId"),gameId,teamId);
    }

    @PostMapping("/{gameId}/teams/{teamId}/sub-applicants") // 용병 신청
    public String SubApply(@RequestBody Map<String, Long> logInUser,@PathVariable Long gameId, @PathVariable Long teamId){
        return subService.subApplicant(logInUser.get("userId"),gameId,teamId);
    }

    @PostMapping("/{gameId}/teams/{teamId}/subs") // 용병 수락
    public String SubAccept(@RequestBody Map<String, Long> logInUser, @PathVariable Long gameId, @PathVariable Long teamId){
        return subService.subAccept(logInUser.get("userId"),gameId,teamId);
    }

    @PostMapping("/{gameId}/teams/{teamId}/sub-applicants/{userId}")//용병 거절
    public String subReject(@RequestBody Map<String, Long> logInUser, @PathVariable Long gameId, @PathVariable Long teamId){
        return subService.subReject(logInUser.get("userId"),gameId,teamId);
    }


}
