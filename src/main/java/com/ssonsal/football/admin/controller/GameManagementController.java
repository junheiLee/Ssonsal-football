package com.ssonsal.football.admin.controller;

import com.ssonsal.football.admin.exception.AdminErrorCode;
import com.ssonsal.football.admin.exception.NotFoundException;
import com.ssonsal.football.admin.service.GameService;
import com.ssonsal.football.global.exception.CustomException;
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

    /**
     *  관리자 페이지에서 모든 게임 글 리스트를 가져온다
     * @param model
     * @return 게임 글 리스트
     */
    @GetMapping("/game")
    public String adminGame(Model model) {

        Long user = 1L;

        if (user == null) {
            throw new CustomException(AdminErrorCode.USER_NOT_AUTHENTICATION);
        }

        model.addAttribute("gameList", gameService.gameList());
        return "admin_game";
    }

    /**
     *  관리자 페이지에서 모든 용병 글 리스트를 가져온다
     * @param model
     * @return 용병 글 리스트
     */
    @GetMapping("/game/sub")
    public String adminSub(Model model) {
        model.addAttribute("subList", gameService.gameList());
        return "admin_sub";

    }
}
