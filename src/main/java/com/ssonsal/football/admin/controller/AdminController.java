package com.ssonsal.football.admin.controller;

import com.ssonsal.football.admin.dto.request.StatsDTO;
import com.ssonsal.football.admin.exception.AdminErrorCode;
import com.ssonsal.football.admin.exception.AdminSuccessCode;
import com.ssonsal.football.admin.service.GameManagementServiceImpl;
import com.ssonsal.football.admin.service.StatsServiceImpl;
import com.ssonsal.football.admin.service.UserManagementServiceImpl;
import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.global.util.formatter.DataResponseBodyFormatter;
import com.ssonsal.football.global.util.formatter.ResponseBodyFormatter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ssonsal.football.game.util.Transfer.objectToMap;

import java.time.LocalDate;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "AdminView", description = "Admin View")
public class AdminController {

    private final GameManagementServiceImpl gameServiceImpl;

    private final StatsServiceImpl statsServiceImpl;

    private final UserManagementServiceImpl userServiceImpl;

    /**
     * 관리자 페이지에서 모든 회원 리스트를 가져온다
     *
     * @return 회원 글 리스트
     */
    @GetMapping("/user")
    public ResponseEntity<ResponseBodyFormatter> adminUser() {

        Long userId = 2L;

        if (!userServiceImpl.isAdmin(userId)) {
            throw new CustomException(AdminErrorCode.USER_NOT_AUTHENTICATION);
        }

        return DataResponseBodyFormatter.put(AdminSuccessCode.PAGE_ALTER_SUCCESS, objectToMap("userList",userServiceImpl.userList()));

    }

    /**
     * 관리자 페이지에서 모든 게임 글 리스트를 가져온다
     *
     * @return 게임 글 리스트
     */
    @GetMapping("/game")
    public ResponseEntity<ResponseBodyFormatter> adminGame() {

        Long userId = 2L;

        if (!userServiceImpl.isAdmin(userId)) {
            throw new CustomException(AdminErrorCode.USER_NOT_AUTHENTICATION);
        }

        return DataResponseBodyFormatter.put(AdminSuccessCode.PAGE_ALTER_SUCCESS, objectToMap("gameList",gameServiceImpl.gameList()));
    }

/*    *//**
     * 관리자 페이지에서 모든 용병 글 리스트를 가져온다
     *
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
     *
     * @return 이번달 통계와 이번달의 하루 통계를 보여준다
     */
    @GetMapping("/stats")
    public ResponseEntity<ResponseBodyFormatter> getGameStats() {

        Long userId = 2L;

        if (!userServiceImpl.isAdmin(userId)) {
            throw new CustomException(AdminErrorCode.USER_NOT_AUTHENTICATION);
        }

        // 현재 날짜 가져오기
        LocalDate currentDate = LocalDate.now();

        StatsDTO monthStats = statsServiceImpl.monthStats(currentDate);
        Map<LocalDate, StatsDTO> monthlyDailyStats = statsServiceImpl.monthlyDailyStats(currentDate);


        return DataResponseBodyFormatter.put(
                AdminSuccessCode.PAGE_ALTER_SUCCESS,
                Map.of("monthStats", monthStats, "monthlyDailyStats", monthlyDailyStats)
        );
    }


    /**
     *  관리자 체크
     *  userRole ==0 이면 에러
     *  userRole==1 이면 성공
     *
     * @return
     */
    @GetMapping("/isAdmin")
    public ResponseEntity<ResponseBodyFormatter> isAdmin() {
        Long userId = 2L;

        if (!userServiceImpl.isAdmin(userId)) {
            throw new CustomException(AdminErrorCode.ADMIN_AUTH_FAILED);
        }

        return ResponseBodyFormatter.put(AdminSuccessCode.ADMIN_AUTH_SUCCESS);
    }

    @GetMapping("/main")
    public ResponseEntity<ResponseBodyFormatter> getUserAndGameStats() {

        Long userId = 2L;

        if (!userServiceImpl.isAdmin(userId)) {
            throw new CustomException(AdminErrorCode.ADMIN_AUTH_FAILED);
        }

        return DataResponseBodyFormatter.put(AdminSuccessCode.USER_COUNT_SUCCESS, objectToMap("dailyStats",statsServiceImpl.getAdminStats()));

    }

}
