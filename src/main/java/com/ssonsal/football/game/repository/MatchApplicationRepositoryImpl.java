package com.ssonsal.football.game.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssonsal.football.game.dto.response.MatchTeamResponseDto;
import com.ssonsal.football.game.dto.response.QMatchTeamResponseDto;

import javax.persistence.EntityManager;

import static com.ssonsal.football.game.entity.ApplicantStatus.APPROVAL;
import static com.ssonsal.football.game.entity.QMatchApplication.matchApplication;
import static com.ssonsal.football.team.entity.QTeam.team;

public class MatchApplicationRepositoryImpl implements MatchApplicationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MatchApplicationRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public MatchTeamResponseDto searchMatchTeamDto(Long matchTeamId) {
        return queryFactory
                .select(new QMatchTeamResponseDto(team.id, team.logoUrl, team.name, team.skillScore,
                        matchApplication.game.id, matchApplication.uniform, matchApplication.subCount))
                .from(matchApplication)
                .join(matchApplication.team, team)
                .where(matchApplication.id.eq(matchTeamId),
                        matchApplication.applicationStatus.eq(APPROVAL.getDescription()))
                .fetchOne();
    }
}
