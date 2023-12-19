package com.ssonsal.football.game.util;

public final class GameConstant {

    private GameConstant() {
    }

    /* json key 사용 string */

    /*Game*/
    public static final String CREATED_GAME_ID = "createdGameId";
    public static final String GAME_INFO = "gameInfo";
    public static final String GAME_RESULT = "gameResult";
    public static final String GAMES = "games";
    public static final String GAME_ID = "gameId";


    /*MatchTeam*/
    public static final String MATCH_TEAM_INFO = "matchTeamInfo";
    public static final String CONFIRMED_GAME_ID = "confirmedGameId";

    /*MatchApplicant*/
    public static final String MATCH_APPLICATION_ID = "matchApplicationId";
    public static final String REJECTED_MATCH_APPLICATION_ID = "rejectedMatchApplicationId";
    public static final String MATCH_APPLICATIONS = "matchApplications";

    public static final String AWAY = "away";
    public static final String HOME = "home";

    public static final String LOGIN_USER = "loginUser";
    public static final String SUB_APPLICANTS = "subApplicants";
    public static final String SUBS = "subs";

    public static final String USER_ID = "userId";
    public static final String TEAM_ID = "teamId";
    public static final String TEAM_RECORD_ID = "teamRecordId";
    public static final String SUB_APPLICANT_ID = "subApplicantId";

    public static final String CLOSED_MATCH_APPLICATION_ID = "closeMatchApplicationId";
    public static final String REJECTED_SUB_USER_ID = "rejectedSubUserId";
    public static final String CREATED_SUB_ID = "createdSubId";


    /* code */
    public static final int NOT_DELETED = 0;

    /* count */
    public static final int ZERO = 0;

    /* LocalDateTime 관련 */
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";
    public static final String SCHEDULE_FORMAT = "yy년 M월 d일 HH시";
}
