package com.ssonsal.football.team.repository;

import com.ssonsal.football.team.entity.TeamRecord;

import java.util.List;

public interface TeamRecordRepositoryCustom {

    List<TeamRecord> determineRank();
}
