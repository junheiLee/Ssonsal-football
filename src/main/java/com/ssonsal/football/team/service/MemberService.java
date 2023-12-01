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

}
