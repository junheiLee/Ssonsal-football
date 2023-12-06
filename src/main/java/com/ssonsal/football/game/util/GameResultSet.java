package com.ssonsal.football.game.util;

import com.ssonsal.football.team.entity.TeamRecord;

import java.util.Arrays;
import java.util.function.Supplier;

import static com.ssonsal.football.game.util.TeamResult.*;

public enum GameResultSet {

    HOME_WIN_AWAY_LOSE(WIN, LOSE, () -> TeamRecord.builder().winCount(1).build(), () -> TeamRecord.builder().loseCount(1).build()),
    HOME_LOSE_AWAY_WIN(LOSE, WIN, () -> TeamRecord.builder().loseCount(1).build(), () -> TeamRecord.builder().winCount(1).build()),
    HOME_DRAW_AWAY_DRAW(DRAW, DRAW, () -> TeamRecord.builder().drawCount(1).build(), () -> TeamRecord.builder().drawCount(1).build());

    private final TeamResult home;
    private final TeamResult away;
    private final Supplier<TeamRecord> homeRecordEntity;
    private final Supplier<TeamRecord> awayRecordEntity;

    GameResultSet(TeamResult home, TeamResult away, Supplier<TeamRecord> homeRecordEntity, Supplier<TeamRecord> awayRecordEntity) {
        this.home = home;
        this.away = away;
        this.homeRecordEntity = homeRecordEntity;
        this.awayRecordEntity = awayRecordEntity;
    }

    public static TeamRecord getHomeRecordEntity(TeamRecord target, TeamResult home, TeamResult away) {
        TeamRecord updateRecord = Arrays.stream(values())
                .filter(resultSet -> resultSet.home.equals(home) && resultSet.away.equals(away))
                .findAny()
                .get()
                .homeRecordEntity.get();
        return target.update(updateRecord);
    }

    public static TeamRecord getAwayRecordEntity(TeamRecord target, TeamResult home, TeamResult away) {
        TeamRecord updateRecord =  Arrays.stream(values())
                .filter(resultSet -> resultSet.home.equals(home) && resultSet.away.equals(away))
                .findAny()
                .get()
                .awayRecordEntity.get();
        return target.update(updateRecord);
    }


}
