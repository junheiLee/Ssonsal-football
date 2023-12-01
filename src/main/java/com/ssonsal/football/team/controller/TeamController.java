package com.ssonsal.football.team.controller;

import com.ssonsal.football.team.entity.Role;
import com.ssonsal.football.team.service.MemberService;
import com.ssonsal.football.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


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
    public String AllList(Model model) {

        // 추후 토큰값으로 교체할 부분임
        Long user = 1L;

        if (user == null) {
            model.addAttribute("userlevel", Role.GUEST);
        } else {
            model.addAttribute("userlevel", memberService.isUserLevel(user));
        }

        model.addAttribute("teams", teamService.findAllList());

        return "team/teamList";
    }

    /**
     * 모집중인 팀 리스트를 가져온다.
     *
     * @return 모집중인 팀 목록 정보 리스트
     */
    @GetMapping("/recruit")
    public String RecruitList(Model model) {

        model.addAttribute("teams", teamService.findRecruitList());

        // 추후 토큰값으로 교체할 부분임
        Long user = 1L;

        if (user == null) {
            model.addAttribute("userlevel", Role.GUEST);
        } else {
            model.addAttribute("userlevel", memberService.isUserLevel(user));
        }

        return "team/teamList";
    }

    /**
     * 특정 키워드를 주면, 키워드에 맞는 팀 이름을 찾아서 가져온다.
     *
     * @param keyword 검색어
     * @return 팀명 검색어에 맞는 팀 목록 정보 리스트
     */
    @GetMapping("/search")
    public String SearchList(@RequestParam String keyword, Model model) {

        model.addAttribute("teams", teamService.searchName(keyword));

        // 추후 토큰값으로 교체할 부분임
        Long user = 1L;

        if (user == null) {
            model.addAttribute("userlevel", Role.GUEST);
        } else {
            model.addAttribute("userlevel", memberService.isUserLevel(user));
        }

        return "team/teamList";
    }

    /**
     * 특정 팀 상세정보를 가져온다.
     *
     * @param teamId 팀 아이디
     * @return 팀 아이디에 맞는 팀 정보
     */
    @GetMapping("/{teamId}")
    public String showDetail(@PathVariable Long teamId, Model model) {

        // 추후 토큰값으로 교체할 부분임
        Long user = 1L;

        if (user == null) {
            model.addAttribute("userlevel", Role.GUEST);
        } else {
            model.addAttribute("userlevel", memberService.isUserLevel(teamId, user));
        }

        model.addAttribute("detail", teamService.findDetail(teamId));
        model.addAttribute("members", teamService.findMemberList(teamId));

        return "team/teamDetail";
    }

    /**
     * 팀 신청자 목록과 팀 회원목록을 가져온다.
     *
     * @param teamId 팀 아이디
     * @return 팀 아이디에 맞는 회원 정보와 팀 신청자 정보
     */
    @GetMapping("/{teamId}/managers")
    public String showManageList(@PathVariable Long teamId, Model model) {

        // 추후 토큰값으로 교체할 부분임
        Long user = 1L;

        if (user == null) {
            return "index";
        } else {
            if (!memberService.isTeamLeader(teamId, user)) {
                return "index";
            }
        }

        model.addAttribute("manage", teamService.findManageList(teamId));

        return "team/teamManage";
    }

}
