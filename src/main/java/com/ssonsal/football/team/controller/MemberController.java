package com.ssonsal.football.team.controller;

import com.ssonsal.football.global.account.Account;
import com.ssonsal.football.global.account.CurrentUser;
import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.global.util.SuccessCode;
import com.ssonsal.football.global.util.formatter.DataResponseBodyFormatter;
import com.ssonsal.football.global.util.formatter.ResponseBodyFormatter;
import com.ssonsal.football.team.exception.TeamErrorCode;
import com.ssonsal.football.team.exception.TeamSuccessCode;
import com.ssonsal.football.team.service.MemberService;
import com.ssonsal.football.team.service.TeamApplyService;
import com.ssonsal.football.team.service.TeamRejectService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ssonsal.football.global.util.transfer.Transfer.toMap;
import static com.ssonsal.football.team.util.TeamConstant.TEAM_NAME;
import static com.ssonsal.football.team.util.TeamConstant.USER_NAME;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/members")
@Tag(name = "Member", description = "Member API")
public class MemberController {

    private final TeamRejectService teamRejectService;
    private final TeamApplyService teamApplyService;
    private final MemberService memberService;

    /**
     * 팀에 가입 신청을 보냅니다.
     *
     * @param teamId 팀 아이디
     * @return 성공 여부
     */
    @PostMapping("/{teamId}/application")
    public ResponseEntity<ResponseBodyFormatter> createUserApply(@PathVariable Long teamId, @CurrentUser Account account) {

        Long user = account.getId();

        if (!memberService.isTeamRecruit(teamId)) {
            throw new CustomException(TeamErrorCode.TEAM_NOT_RECRUIT);
        } else if (memberService.isUserOtherApply(user)) {
            throw new CustomException(TeamErrorCode.HAS_OTHER_APPLY);
        } else if (teamRejectService.isUserRejected(user, teamId)) {
            throw new CustomException(TeamErrorCode.BANNED_USER);
        } else if (memberService.hasAnyTeam(user)) {
            throw new CustomException(TeamErrorCode.ALREADY_IN_TEAM);
        }

        return DataResponseBodyFormatter.put(TeamSuccessCode.USER_TEAM_APPLY, toMap(TEAM_NAME, teamApplyService.createUserApply(user, teamId)));
    }

    /**
     * 팀에 가입 신청을 취소합니다.
     *
     * @param teamId 팀 아이디
     * @return 성공 여부
     */
    @DeleteMapping("/{teamId}/application")
    public ResponseEntity<ResponseBodyFormatter> deleteUserApply(@PathVariable Long teamId, @CurrentUser Account account) {

        Long user = account.getId();

        if (!memberService.isUserApply(user, teamId)) {
            throw new CustomException(TeamErrorCode.USER_NOT_APPLY);
        }

        teamApplyService.deleteUserApply(user);

        return ResponseBodyFormatter.put(SuccessCode.SUCCESS);
    }

    /**
     * 팀에서 탈퇴합니다.
     *
     * @param teamId 팀 아이디
     * @return 성공 여부
     */
    @DeleteMapping("/{teamId}/team")
    public ResponseEntity<ResponseBodyFormatter> leaveTeam(@PathVariable Long teamId, @CurrentUser Account account) {

        Long user = account.getId();

        if (memberService.isTeamLeader(teamId, user)) {
            throw new CustomException(TeamErrorCode.LEADER_IN_GROUP);
        } else if (!memberService.hasAnyTeam(user)) {
            throw new CustomException(TeamErrorCode.USER_NOT_TEAM);
        } else if (!memberService.isUserTeamMember(teamId, user)) {
            throw new CustomException(TeamErrorCode.USER_OTHER_TEAM);
        }

        return DataResponseBodyFormatter.put(SuccessCode.SUCCESS, toMap(TEAM_NAME, memberService.leaveTeam(teamId, user)));
    }

    /**
     * 유저의 신청을 수락합니다.
     *
     * @param teamId 팀 아이디
     * @param userId 신청자 아이디
     * @return 신청자 닉네임
     */
    @PostMapping("/{teamId}/application/{userId}")
    public ResponseEntity<ResponseBodyFormatter> userApplyAccept(@PathVariable Long teamId, @PathVariable Long userId, @CurrentUser Account account) {

        Long user = account.getId();

        if (!memberService.isTeamLeader(teamId, user)) {
            throw new CustomException(TeamErrorCode.MEMBER_NOT_LEADER);
        } else if (!memberService.isUserApply(userId, teamId)) {
            throw new CustomException(TeamErrorCode.USER_NOT_APPLY);
        }


        return DataResponseBodyFormatter.put(SuccessCode.SUCCESS, toMap(USER_NAME, teamApplyService.userApplyAccept(userId, teamId)));
    }

    /**
     * 유저의 신청을 거절합니다.
     *
     * @param teamId 팀 아이디
     * @param userId 신청자 아이디
     * @return 신청자 닉네임
     */
    @DeleteMapping("/{teamId}/application/{userId}")
    public ResponseEntity<ResponseBodyFormatter> userApplyReject(@PathVariable Long teamId, @PathVariable Long userId, @CurrentUser Account account) {

        Long user = account.getId();

        if (!memberService.isTeamLeader(teamId, user)) {
            throw new CustomException(TeamErrorCode.MEMBER_NOT_LEADER);
        } else if (!memberService.isUserApply(userId, teamId)) {
            throw new CustomException(TeamErrorCode.USER_NOT_APPLY);
        }

        return DataResponseBodyFormatter.put(SuccessCode.SUCCESS, toMap(USER_NAME, teamApplyService.userApplyReject(userId, teamId)));
    }

    /**
     * 팀원에게 팀장직을 위임합니다.
     *
     * @param teamId 팀 아이디
     * @param userId 위임받을 팀원 아이디
     * @return 위임받은 팀장 닉네임
     */
    @PatchMapping("/{teamId}/manager/{userId}")
    public ResponseEntity<ResponseBodyFormatter> delegateLeader(@PathVariable Long teamId, @PathVariable Long userId, @CurrentUser Account account) {

        Long user = account.getId();

        if (!memberService.isTeamLeader(teamId, user)) {
            throw new CustomException(TeamErrorCode.MEMBER_NOT_LEADER);
        } else if (!memberService.isUserTeamMember(teamId, userId)) {
            throw new CustomException(TeamErrorCode.USER_NOT_MEMBER);
        }

        return DataResponseBodyFormatter.put(SuccessCode.SUCCESS, toMap(USER_NAME, memberService.delegateLeader(teamId, userId)));
    }

    /**
     * 팀장 권한으로 팀에서 회원을 퇴출합니다.
     *
     * @param teamId 팀 아이디
     * @param userId 퇴출할 회원의 아이디
     * @return 퇴출당한 회원의 닉네임
     */
    @PostMapping("/{teamId}/manager/{userId}")
    public ResponseEntity<ResponseBodyFormatter> banUser(@PathVariable Long teamId, @PathVariable Long userId, @CurrentUser Account account) {

        Long user = account.getId();

        if (!memberService.isTeamLeader(teamId, user)) {
            throw new CustomException(TeamErrorCode.MEMBER_NOT_LEADER);
        } else if (memberService.isTeamLeader(teamId, userId)) {
            throw new CustomException(TeamErrorCode.CANNOT_REMOVE_LEADER);
        } else if (!memberService.hasAnyTeam(userId)) {
            throw new CustomException(TeamErrorCode.USER_NOT_TEAM);
        } else if (memberService.hasAnyTeam(userId)) {
            if (!memberService.isUserTeamMember(teamId, userId)) {
                throw new CustomException(TeamErrorCode.USER_OTHER_TEAM);
            }
        }

        return DataResponseBodyFormatter.put(SuccessCode.SUCCESS, toMap(USER_NAME, memberService.banUser(userId, teamId)));
    }

    /**
     * 유저 밴 / 거절 기록을 삭제합니다.
     *
     * @param teamId 팀 아이디
     * @param userId 유저 아이디
     * @return 밴이 풀린 회원의 닉네임
     */
    @DeleteMapping("/{teamId}/manager/{userId}")
    public ResponseEntity<ResponseBodyFormatter> userBanCancel(@PathVariable Long teamId, @PathVariable Long userId, @CurrentUser Account account) {

        Long user = account.getId();

        if (!memberService.isTeamLeader(teamId, user)) {
            throw new CustomException(TeamErrorCode.MEMBER_NOT_LEADER);
        } else if (!teamRejectService.isUserRejected(userId, teamId)) {
            throw new CustomException(TeamErrorCode.USER_NOT_REJECT);
        }

        return DataResponseBodyFormatter.put(SuccessCode.SUCCESS, toMap(USER_NAME, memberService.banUserCancel(teamId, userId)));
    }
}
