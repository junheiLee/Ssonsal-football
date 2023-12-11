package com.ssonsal.football.game.controller;

import com.ssonsal.football.game.dto.request.SubRecordDto;
import com.ssonsal.football.game.service.SubService;
import com.ssonsal.football.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/games")
public class SubRestController {
    @Autowired
    private SubService subService;

    @GetMapping("/subs/{userId}") // 유저가 참여한 게임 목록
    public List<SubRecordDto> getSubsByUserId(@PathVariable Long userId) {

        return subService.getSubRecordsByUserId(userId);
    }

    @PostMapping("/{gameId}/teams/{teamId}/sub-applicants") // 용병 신청
    public String SubApply(@PathVariable Long gameId, @PathVariable Long teamId){
        Long userId = 1L; // 용병 신청한 사람 아이디
        return subService.subApplicant(userId,gameId,teamId);
    }

    @PostMapping("/{gameId}/teams/{teamId}/subs") // 용병 수락
    public String SubAccept(@RequestBody Map<String, Long> subInfo, @PathVariable Long gameId, @PathVariable Long teamId){

        return subService.subAccept(subInfo.get("userId"),gameId,teamId);
    }

    @DeleteMapping("/{gameId}/teams/{teamId}/sub-applicants/{userId}")//용병 거절
    public String subReject(@RequestBody Map<String, Long> subInfo, @PathVariable Long gameId, @PathVariable Long teamId){
        return subService.subReject(subInfo.get("userId"),gameId,teamId);
    }


}
