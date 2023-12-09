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

    String leaveTeam(Long teamId, Long userId);

    String delegateLeader(Long teamId, Long userId);

    String banUser(Long userId, Long teamId);

    String banUserCancel(Long teamId, Long userId);

    boolean isTeamRecruit(Long teamId);
}
