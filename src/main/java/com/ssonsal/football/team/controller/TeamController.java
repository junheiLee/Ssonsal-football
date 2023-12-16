package com.ssonsal.football.team.controller;

import com.ssonsal.football.game.util.Transfer;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static com.ssonsal.football.game.util.Transfer.objectToMap;
import static com.ssonsal.football.team.util.TeamConstant.*;


@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin("*")
public class TeamController {

    private final TeamService teamService;
    private final MemberService memberService;

    /**
     * 모든 팀 리스트를 반환한다.
     *
     * @return 팀 목록 정보 리스트
     */
    @GetMapping
    public ResponseEntity<ResponseBodyFormatter> findAllTeams() {

        return DataResponseBodyFormatter.put(TeamSuccessCode.DATA_TRANSFER_SUCCESS, objectToMap(TEAMS, teamService.findAllTeams()));
    }

    /**
     * 모집중인 팀 리스트를 반환한다.
     *
     * @return 모집중인 팀 목록 정보 리스트
     */
    @GetMapping("/recruit")
    public ResponseEntity<ResponseBodyFormatter> findAllRecruitTeams() {

        return DataResponseBodyFormatter.put(TeamSuccessCode.DATA_TRANSFER_SUCCESS, objectToMap(TEAMS, teamService.findRecruitList()));
    }

    /**
     * 특정 키워드를 주면, 키워드에 맞는 팀 이름을 찾아서 반환한다.
     *
     * @param keyword 검색어
     * @return 팀명 검색어에 맞는 팀 목록 정보 리스트
     */
    @GetMapping("/search")
    public ResponseEntity<ResponseBodyFormatter> findAllSearchTeams(@RequestParam String keyword) {

        return DataResponseBodyFormatter.put(TeamSuccessCode.DATA_TRANSFER_SUCCESS, objectToMap(TEAMS, teamService.findSearchList(keyword)));
    }

    /**
     * 특정 팀 상세정보를 반환한다.
     *
     * @param teamId 팀 아이디
     * @return 팀 아이디에 맞는 팀 정보
     */
    @GetMapping("/{teamId}")
    public ResponseEntity<ResponseBodyFormatter> findDetailOfTeam(@PathVariable Long teamId) {

        // 추후 토큰값으로 교체할 부분임
        Long user = 1L;

        String userLevel = "";

        if (user == null) {
            userLevel = Role.GUEST.getRole();
        } else {
            userLevel = memberService.isUserLevel(teamId, user).getRole();
        }

        Map<String, Object> details = new HashMap<>();
        details.put(USER_LEVEL, userLevel);
        details.put(DETAIL, teamService.findTeamDetail(teamId));
        details.put(MEMBERS, teamService.findMemberList(teamId));

        return DataResponseBodyFormatter.put(TeamSuccessCode.DATA_TRANSFER_SUCCESS, details);
    }

    /**
     * 팀 관리 목록을 반환한다.
     *
     * @param teamId 팀 아이디
     * @return 팀 아이디에 맞는 회원 정보와 팀 신청자 정보와 밴 유저 목록
     */
    @GetMapping("/{teamId}/managers")
    public ResponseEntity<ResponseBodyFormatter> findManageListOfTeam(@PathVariable Long teamId) {

        // 추후 토큰값으로 교체할 부분임
        Long user = 1L;

        if (user == null) {
            throw new CustomException(TeamErrorCode.USER_NOT_AUTHENTICATION);
        } else {
            if (!memberService.isTeamLeader(teamId, user)) {
                throw new CustomException(TeamErrorCode.MEMBER_NOT_LEADER);
            }
        }

        return DataResponseBodyFormatter.put(TeamSuccessCode.DATA_TRANSFER_SUCCESS, teamService.findManageList(teamId));
    }

    /**
     * 팀을 생성하고 팀 번호와 이름을 반환한다.
     *
     * @param teamCreateDto 유저가 입력한 form 데이터
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
     * 기존 팀 정보를 반환한다.
     *
     * @param teamId 팀 아이디
     * @return teamEditFormDto 기존 팀 정보 DTO
     */
    @GetMapping("/{teamId}/edit")
    public ResponseEntity<ResponseBodyFormatter> editForm(@PathVariable Long teamId) {

        Long user = 1L;

        if (user == null) {
            throw new CustomException(TeamErrorCode.USER_NOT_AUTHENTICATION);
        } else if (!memberService.isTeamLeader(teamId, user)) {
            throw new CustomException(TeamErrorCode.MEMBER_NOT_LEADER);
        }

        return DataResponseBodyFormatter.put(TeamSuccessCode.DATA_TRANSFER_SUCCESS, objectToMap(FORM, teamService.findTeamInfo(teamId)));
    }

    /**
     * 팀 정보를 수정하고 ID값을 반환한다.
     *
     * @param teamEditDto 팀 수정 form 데이터
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

        return DataResponseBodyFormatter.put(TeamSuccessCode.LEADER_EDIT_SUCCESS, Transfer.longIdToMap(TEAM_ID, teamService.editTeam(teamEditDto)));
    }
}
