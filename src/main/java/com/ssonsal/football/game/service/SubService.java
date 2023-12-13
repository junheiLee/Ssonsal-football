package com.ssonsal.football.game.service;


import com.ssonsal.football.game.entity.SubApplicant;

import java.util.List;

public interface SubService {
    List<SubApplicant> list(Long gameId, Long teamId);
}
