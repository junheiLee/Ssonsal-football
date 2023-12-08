package com.ssonsal.football.team.controller;

import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.global.util.formatter.DataResponseBodyFormatter;
import com.ssonsal.football.global.util.formatter.ResponseBodyFormatter;
import com.ssonsal.football.team.dto.request.TeamCreateDto;
import com.ssonsal.football.team.dto.request.TeamEditDto;
import com.ssonsal.football.team.entity.Role;
import com.ssonsal.football.team.exception.TeamErrorCode;
import com.ssonsal.football.team.exception.TeamSuccessCode;
import com.ssonsal.football.team.service.MemberService;
import com.ssonsal.football.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


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
    public String findAllTeams(Model model) {

        // 추후 토큰값으로 교체할 부분임
        Long user = 1L;

        if (user == null) {
            model.addAttribute("userlevel", Role.GUEST);
        } else {
            model.addAttribute("userlevel", memberService.isUserLevel(user));
        }

        model.addAttribute("teams", teamService.findAllTeams());

        return "team/teamList";
    }

    /**
     * 모집중인 팀 리스트를 가져온다.
     *
     * @return 모집중인 팀 목록 정보 리스트
     */
    @GetMapping("/recruit")
    public String findAllRecruitTeams(Model model) {

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
    public String findAllSearchTeams(@RequestParam String keyword, Model model) {

        model.addAttribute("teams", teamService.findSearchList(keyword));

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
    public String findDetailOfTeam(@PathVariable Long teamId, Model model) {

        // 추후 토큰값으로 교체할 부분임
        Long user = 1L;

        if (user == null) {
            model.addAttribute("userlevel", Role.GUEST);
        } else {
            model.addAttribute("userlevel", memberService.isUserLevel(teamId, user));
        }

        model.addAttribute("detail", teamService.findTeamDetail(teamId));
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
    public String findManageListOfTeam(@PathVariable Long teamId, Model model) {

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

    /**
     * 신규 팀을 생성하는 페이지로 이동한다.
     */
    @GetMapping("/form")
    public String createForm() {

        Long user = 1L;

        if (user == null) {
            return "login";
        } else if (memberService.hasAnyTeam(user)) {
            throw new CustomException(TeamErrorCode.MEMBER_ALREADY_TEAM);
        }

        return "teamCreate";
    }

    /**
     * 팀을 생성한다.
     *
     * @param teamCreateDto
     * @return 생성된 팀 번호,팀 명
     */
    @PostMapping
    @ResponseBody
    public ResponseEntity<ResponseBodyFormatter> createTeam(@Valid @ModelAttribute TeamCreateDto teamCreateDto) {

        Long user = 1L;

        if (user == null) {
            throw new CustomException(TeamErrorCode.USER_NOT_AUTHENTICATION);
        } else if (memberService.hasAnyTeam(user)) {
            throw new CustomException(TeamErrorCode.MEMBER_ALREADY_TEAM);
        } else if (memberService.isUserOtherApply(user)) {
            throw new CustomException(TeamErrorCode.USER_ALREADY_APPLY);
        } else if (teamService.checkNameDuplicate(teamCreateDto.getName())) {
            throw new CustomException(TeamErrorCode.DUPLICATE_TEAM_NAME);
        }

        return DataResponseBodyFormatter.put(TeamSuccessCode.USER_TEAM_CREATED, teamService.createTeam(teamCreateDto, user));
    }

    /**
     * 팀 정보 수정 페이지를 불러온다.
     *
     * @param teamId
     * @return teamEditFormDto 팀 정보 DTO
     */
    @GetMapping("/{teamId}/edit")
    public String editForm(Model model, @PathVariable Long teamId) {

        Long user = 1L;

        if (user == null) {
            return "login";
        } else if (!memberService.isTeamLeader(teamId, user)) {
            return "redirect:/teams/" + teamId;
        }

        model.addAttribute("form", teamService.findTeamInfo(teamId));

        return "teamEdit";
    }

    /**
     * 팀 정보를 수정한다.
     *
     * @param teamEditDto
     * @return 팀 아이디 값
     */
    @PatchMapping
    @ResponseBody
    public ResponseEntity<ResponseBodyFormatter> editTeam(@Valid @ModelAttribute TeamEditDto teamEditDto) {

        Long user = 1L;

        if (user == null) {
            throw new CustomException(TeamErrorCode.USER_NOT_AUTHENTICATION);
        } else if (!memberService.isTeamLeader(teamEditDto.getId(), user)) {
            throw new CustomException(TeamErrorCode.MEMBER_NOT_LEADER);
        } else if (teamService.checkNameDuplicate(teamEditDto.getName(), teamEditDto.getId())) {
            throw new CustomException(TeamErrorCode.DUPLICATE_TEAM_NAME);
        }

        return DataResponseBodyFormatter.put(TeamSuccessCode.LEADER_EDIT_SUCCESS, teamService.editTeam(teamEditDto));
    }
}
