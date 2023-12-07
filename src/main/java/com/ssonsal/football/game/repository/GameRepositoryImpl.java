package com.ssonsal.football.game.repository;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssonsal.football.game.dto.response.GameListResponseDto;
import com.ssonsal.football.game.dto.response.QGameListResponseDto;

import javax.persistence.EntityManager;
import java.util.List;

import static com.ssonsal.football.game.entity.QGame.game;
import static com.ssonsal.football.game.entity.QMatchApplication.matchApplication;

public class GameRepositoryImpl implements GameRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public GameRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<GameListResponseDto> searchAllGameForSub() {
        return queryFactory
                .select(new QGameListResponseDto(game.id, game.schedule.stringValue(), game.region,
                        game.stadium, game.vsFormat, game.gender, game.rule, game.account))
                .from(game)
                .where(JPAExpressions.selectFrom(matchApplication).where(matchApplication.game.eq(game),
                        matchApplication.subCount.gt(0)).exists())
                .fetch();
    }

}
