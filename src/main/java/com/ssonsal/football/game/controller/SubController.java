package com.ssonsal.football.game.controller;

import com.ssonsal.football.game.dto.request.SubInTeamDto;
import com.ssonsal.football.game.service.SubService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/games")
@Tag(name = "Sub", description = "Sub API")
public class SubController {

    private final SubService subService;

    @GetMapping("/{gameId}/teams/{teamId}/subs") // 해당 팀에 대한 용병 (한 게임 한정)
    public List<SubInTeamDto> getTeamSubList(@PathVariable Long gameId, @PathVariable Long teamId) {
        return subService.getTeamSubList(gameId, teamId);
    }


    @PostMapping("/{gameId}/teams/{teamId}/subs") // 용병 수락
    public String SubAccept(@RequestBody Map<String, Long> logInUser, @PathVariable Long gameId, @PathVariable Long teamId) {
        return subService.subAccept(logInUser.get("userId"), gameId, teamId);
    }


}
