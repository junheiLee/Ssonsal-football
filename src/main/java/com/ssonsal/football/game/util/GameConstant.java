package com.ssonsal.football.game.util;

public final class GameConstant {

    private GameConstant() {
    }

    /* json key 사용 string */
    public static final String GAME = "game";
    public static final String AWAY = "away";
    public static final String HOME = "home";
    public static final String MATCH_TEAM = "matchTeam";

    public static final String USER_INFO = "userInfo";

    public static final String GAMES = "games";
    public static final String MATCH_APPLICATIONS = "matchApplications";

    public static final String USER_ID = "userId";
    public static final String GAME_ID = "gameId";
    public static final String TEAM_ID = "teamId";
    public static final String TEAM_RECORD_ID = "teamRecordId";

    public static final String CONFIRMED_GAME_ID = "confirmedGameId";
    public static final String MATCH_APPLICATION_ID = "matchApplicationId";
    public static final String REJECTED_MATCH_APPLICATION_ID = "rejectedMatchApplicationId";

    /* code */
    public static final int DELETED = 1;
    public static final int NOT_DELETED = 0;

    /* LocalDateTime 관련 */
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String SCHEDULE_FORMAT = "yy년 M월 d일 HH시";
}
