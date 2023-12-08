package com.ssonsal.football.team.service;

import com.ssonsal.football.team.entity.Role;

public interface MemberService {

    Role isUserLevel(Long userId);

    Role isUserLevel(Long teamId, Long userId);

    boolean isUserOtherApply(Long userId);

    boolean hasAnyTeam(Long userId);

    boolean isTeamLeader(Long teamId, Long userId);

    boolean isUserTeamMember(Long teamId, Long userId);

    boolean isUserApply(Long userId, Long teamId);

    String userLeaveTeam(Long teamId, Long userId);

    String leaderDelegate(Long teamId, Long userId);

    String userBan(Long userId, Long teamId);

    String userBanCancel(Long teamId,Long userId);
}
