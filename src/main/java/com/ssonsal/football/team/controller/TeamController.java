package com.ssonsal.football.team.controller;

import com.ssonsal.football.team.entity.Role;
import com.ssonsal.football.team.service.MemberService;
import com.ssonsal.football.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/teams")
@RequiredArgsConstructor
@Slf4j
public class TeamController {

    private final TeamService teamService;
    private final MemberService memberService;

    /**
     * 모든 팀 리스트를 가져온다.
     *
     * @return 팀 목록 정보 리스트
     */
    @GetMapping
    public String showList(Model model) {

        // 추후 토큰값으로 교체할 부분임
        Long user = 1L;

        if (user == null) {
            model.addAttribute("userlevel", Role.GUEST);
        } else {
            model.addAttribute("userlevel", memberService.isUserLevel(user));
        }

        model.addAttribute("teams", teamService.findList());

        return "teamList";
    }

    /**
     * 모집중인 팀 리스트를 가져온다.
     *
     * @return 모집중인 팀 목록 정보 리스트
     */
    @GetMapping("/recruit")
    public String showRecruitList(Model model) {

        model.addAttribute("teams", teamService.findRecruitList());

        // 추후 토큰값으로 교체할 부분임
        Long user = 1L;

        if (user == null) {
            model.addAttribute("userlevel", Role.GUEST);
        } else {
            model.addAttribute("userlevel", memberService.isUserLevel(user));
        }

        return "teamList";
    }


}
