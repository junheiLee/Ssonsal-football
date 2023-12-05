package com.ssonsal.football.game.service;


import com.ssonsal.football.game.dto.request.SubRecordDto;
import com.ssonsal.football.game.entity.Sub;
import com.ssonsal.football.game.entity.SubApplicant;

import java.util.List;

public interface SubService {

    List<SubRecordDto> getSubRecordsByUserId(Long userId);
}
