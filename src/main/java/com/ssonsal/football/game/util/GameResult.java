package com.ssonsal.football.game.util;

import com.ssonsal.football.game.entity.Game;
import com.ssonsal.football.game.exception.GameErrorCode;
import com.ssonsal.football.game.exception.MatchErrorCode;
import com.ssonsal.football.global.exception.CustomException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum GameResult {

    WIN("승", 3),
    LOSE("패", 1),
    DRAW("무", 2),
    END("종료", 4);

    private final String ko;
    private final Integer score;

    GameResult(String ko, Integer score) {
        this.ko = ko;
        this.score = score;
    }

    public static Integer peekScore(String ko) {
        return Arrays.stream(GameResult.values())
                .filter(result -> result.ko.equals(ko))
                .findAny()
                .orElseThrow(() -> new CustomException(MatchErrorCode.IMPOSSIBLE_RESULT))
                .getScore();
    }
}
