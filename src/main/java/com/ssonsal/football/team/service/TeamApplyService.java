package com.ssonsal.football.team.service;

public interface TeamApplyService {

    String createUserApply(Long userId, Long teamId);

    void deleteUserApply(Long userId);

    String userApplyAccept(Long userId, Long teamId);

    String userApplyReject(Long userId, Long teamId);

}
