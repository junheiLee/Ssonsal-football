package com.ssonsal.football.team.service;

import com.ssonsal.football.team.entity.Role;

public interface MemberService {

    /**
     * 유저의 현재 직책에 따라 다른 값을 넘겨준다.
     *
     * @param userId 유저 아이디
     * @return 유저 권한
     */
    Role isUserLevel(Long userId);

    /**
     * 유저의 현재 직책에 따라 다른 값을 넘겨준다.
     * teamId,userId를 이용해 팀장인지,팀원인지,팀이 없는지 구분한다.
     *
     * @param teamId 팀 아이디
     * @param userId 유저 아이디
     * @return 유저 권한
     */
    Role isUserLevel(Long teamId, Long userId);

    /**
     * 유저가 특정 팀에 신청 중 인지 확인합니다.
     *
     * @param userId 유저 아이디
     */
    boolean isUserOtherApply(Long userId);

    /**
     * 유저가 팀을 가지고 있는지 확인합니다.
     *
     * @param userId 유저 아이디
     */
    boolean isUserHasTeam(Long userId);


    /**
     * 유저가 팀장인지 확인합니다.
     *
     * @param teamId 팀 아이디
     * @param userId 유저 아이디
     */
    boolean isTeamLeader(Long teamId, Long userId);

    /**
     * 유저가 특정 팀의 소속인지 확인합니다.
     *
     * @param teamId 팀 아이디
     * @param userId 유저 아이디
     */
    boolean isUserTeamMember(Long teamId, Long userId);

    /**
     * 유저가 해당 팀에 신청 중 인지 확인합니다.
     *
     * @param userId 유저 아이디
     * @param teamId 팀 아이디
     */
    boolean isUserApply(Long userId, Long teamId);

    /**
     * 유저의 팀 정보를 삭제합니다.
     *
     * @param userId 유저 아이디
     * @param teamId 팀 아이디
     * @return 탈퇴하는 팀명
     */
    String userLeaveTeam(Long teamId, Long userId);

    /**
     * 팀장을 변경합니다.
     *
     * @param teamId 팀 아이디
     * @param userId 위임받을 사람 아이디
     * @return 위임받은 사람 닉네임
     */
    String leaderDelegate(Long teamId, Long userId);


    /**
     * 팀에서 회원을 퇴출시킨다.
     *
     * @param userId 퇴출자 아이디
     * @param teamId 팀 아이디
     * @return 퇴출당한 회원 닉네임
     */
    String userBan(Long userId, Long teamId);

}
