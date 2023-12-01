package com.ssonsal.football.team.service;

public interface TeamApplyService {

    void createUserApply(Long userId, Long teamId);

    void deleteUserApply(Long userId);

    String userApplyAccept(Long userId, Long teamId);

    String userApplyReject(Long userId, Long teamId);
}
