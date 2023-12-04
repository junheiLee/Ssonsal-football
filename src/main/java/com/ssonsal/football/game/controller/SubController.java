package com.ssonsal.football.game.controller;

import com.ssonsal.football.game.service.SubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/games")
public class SubController {

    @Autowired
    private SubService subService;

    @GetMapping("/{gameId}/teams/{teamId}/sub-applicants")
    public String SubApplyList(@PathVariable Long gameId, @PathVariable Long teamId,Model model){

        model.addAttribute("list",subService.list(gameId, teamId));
        return "gameDetail";
    }


}