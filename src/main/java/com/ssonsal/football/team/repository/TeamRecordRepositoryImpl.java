package com.ssonsal.football.team.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssonsal.football.team.entity.TeamRecord;

import javax.persistence.EntityManager;
import java.util.List;

import static com.ssonsal.football.team.entity.QTeamRecord.teamRecord;

public class TeamRecordRepositoryImpl implements TeamRecordRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public TeamRecordRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<TeamRecord> determineRank() {
        return queryFactory
                .selectFrom(teamRecord)
                .where(teamRecord.totalGameCount.gt(0))
                .orderBy(teamRecord.point.desc(), teamRecord.winCount.desc(), teamRecord.createdAt.desc())
                .fetch();
    }

}
