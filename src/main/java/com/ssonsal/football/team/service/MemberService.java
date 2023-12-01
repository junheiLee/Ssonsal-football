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
<<<<<<< HEAD
     * 유저의 현재 직책에 따라 다른 값을 넘겨준다.
     *
     * @param userId 유저 아이디
     * @return 유저 권한
     */
    Role isUserLevel(Long teamId, Long userId);

    /**
=======
>>>>>>> 6f7de8312aa9059d359f72e01b9c134dc1e16bf4
     * 유저가 특정 팀에 신청 중 인지 확인합니다.
     *
     * @param userId 유저 아이디
     */
    boolean isUserOtherApply(Long userId);

    /**
     * 유저가 팀을 가지고 있는지 확인합니다. -> null 일때 true
     *
     * @param userId 유저 아이디
     */
    boolean isUserTeamExists(Long userId);

<<<<<<< HEAD
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

=======
>>>>>>> 6f7de8312aa9059d359f72e01b9c134dc1e16bf4
}
