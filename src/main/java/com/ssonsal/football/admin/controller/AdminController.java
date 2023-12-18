package com.ssonsal.football.admin.controller;

import com.ssonsal.football.admin.dto.response.GameDTO;
import com.ssonsal.football.admin.dto.response.StatsDTO;
import com.ssonsal.football.admin.exception.AdminErrorCode;
import com.ssonsal.football.admin.service.GameManagementService;
import com.ssonsal.football.admin.service.StatsService;
import com.ssonsal.football.admin.service.UserManagementService;
import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.global.util.SuccessCode;
import com.ssonsal.football.global.util.formatter.DataResponseBodyFormatter;
import com.ssonsal.football.global.util.formatter.ResponseBodyFormatter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.ssonsal.football.game.util.Transfer.objectToMap;

@RestController
@Slf4j
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "AdminView", description = "Admin View")
public class AdminController {

    private final GameManagementService gameService;

    private final StatsService statsService;

    private final UserManagementService userService;

    /**
     * 관리자 페이지에서 모든 회원 리스트를 가져온다
     *
     * @return 회원 글 리스트
     */
    @GetMapping("/user")
    public ResponseEntity<ResponseBodyFormatter> adminUser() {

        Long userId = 2L;

        if (userId == null) {
            throw new CustomException(AdminErrorCode.USER_NOT_AUTHENTICATION);
        } else if (userService.isAdmin(userId)) {
            throw new CustomException(AdminErrorCode.ADMIN_AUTH_FAILED);
        }
        return DataResponseBodyFormatter.put(SuccessCode.SUCCESS, objectToMap("userList", userService.userList()));

    }

    /**
     * 관리자 페이지에서 모든 게임 글 리스트를 가져온다
     *
     * @return 게임 글 리스트
     */
    @GetMapping("/game")
    public ResponseEntity<ResponseBodyFormatter> adminGame() {

        Long userId = 2L;

        if (userId == null) {
            throw new CustomException(AdminErrorCode.USER_NOT_AUTHENTICATION);
        } else if (userService.isAdmin(userId)) {
            throw new CustomException(AdminErrorCode.ADMIN_AUTH_FAILED);
        }
        List<GameDTO> gameList = gameService.gameList();

        return DataResponseBodyFormatter.put(SuccessCode.SUCCESS, objectToMap("gameList", gameService.gameList()));
    }

    /*    *//**
     * 관리자 페이지에서 모든 용병 글 리스트를 가져온다
     * @param model
     * @return 용병 글 리스트
     *//*
    @GetMapping("/game/sub")
    public String adminSub(Model model) {
        model.addAttribute("subList", gameService.gameList());
        return "admin_sub";

    }*/

    /**
     * 통계 데이터를 가져와 보여준다
     * @return 이번달 통계와 이번달의 하루 통계를 보여준다
     */
    @GetMapping("/stats")
    public ResponseEntity<ResponseBodyFormatter> getGameStats() {

        Long userId = 2L;

        if (userId == null) {
            throw new CustomException(AdminErrorCode.USER_NOT_AUTHENTICATION);
        } else if (userService.isAdmin(userId)) {
            throw new CustomException(AdminErrorCode.ADMIN_AUTH_FAILED);
        }
        // 현재 날짜 가져오기
        LocalDate currentDate = LocalDate.now();

        StatsDTO monthStats = statsService.monthStats(currentDate);
        Map<LocalDate, StatsDTO> monthlyDailyStats = statsService.monthlyDailyStats(currentDate);

        return DataResponseBodyFormatter.put(
                SuccessCode.SUCCESS,
                Map.of("monthStats", monthStats, "monthlyDailyStats", monthlyDailyStats)
        );
    }

    /**
     * 메인 홈페이지 이동
     * 당일 통계들을 구해 메인에 출력한다
     *
     * @return 전체 회원 수
     * 신규 가입자
     * 예정 매치수
     * 올라온 매치글 수 의 당일 통계를 출력
     */
    @GetMapping("/main")
    public ResponseEntity<ResponseBodyFormatter> getUserAndGameStats() {

        Long userId = 2L;

        if (userId == null) {
            throw new CustomException(AdminErrorCode.USER_NOT_AUTHENTICATION);
        } else if (userService.isAdmin(userId)) {
            throw new CustomException(AdminErrorCode.ADMIN_AUTH_FAILED);
        }
        return DataResponseBodyFormatter.put(SuccessCode.SUCCESS, objectToMap("dailyStats", statsService.getAdminStats()));

    }


    /**
     * 관리자 체크
     * userRole ==0 이면 에러
     * userRole==1 이면 성공
     *
     * @return
     */
    @GetMapping("/isAdmin")
    public ResponseEntity<ResponseBodyFormatter> isAdmin() {
        Long userId = 2L;

        if (userService.isAdmin(userId)) {
            throw new CustomException(AdminErrorCode.ADMIN_AUTH_FAILED);
        }

        return ResponseBodyFormatter.put(SuccessCode.SUCCESS);
    }


}
