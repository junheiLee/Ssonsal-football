package com.ssonsal.football.game.service;


import com.ssonsal.football.game.dto.request.SubRecordDto;

import java.util.List;

public interface SubService {

    //용병 신청
    String subApplicant(Long gameId, Long teamId, Long id);

    //용병으로 참여한 기록
    List<SubRecordDto> getSubRecordsByUserId(Long userId);

    String subAccept(Long userId, Long gameId, Long teamId);
}
