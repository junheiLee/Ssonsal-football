package com.ssonsal.football.admin.controller;

import com.ssonsal.football.admin.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/admin")
@RequiredArgsConstructor
public class GameManagementController {

    private final GameService gameService;

    // 게임 목록 목록
    @GetMapping("/game")
    public String adminGame(Model model) {
        model.addAttribute("gameList", gameService.gameList());
        return "admin_game";
    }

    @GetMapping("/game/sub")
    public String adminSub(Model model) {
        model.addAttribute("subList", gameService.gameList());
        return "admin_sub";

    }
}
