package com.ssonsal.football.admin.controller;

import com.ssonsal.football.admin.dto.request.StatsDTO;
import com.ssonsal.football.admin.exception.AdminSuccessCode;
import com.ssonsal.football.admin.service.GameService;
import com.ssonsal.football.admin.service.StatsService;
import com.ssonsal.football.admin.service.UserService;
import com.ssonsal.football.global.util.formatter.DataResponseBodyFormatter;
import com.ssonsal.football.global.util.formatter.ResponseBodyFormatter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "AdminView", description = "Admin View")
public class AdminController {

    private final GameService gameService;

    private final StatsService statsService;

    private final UserService userService;

    /**
     * 관리자 페이지에서 모든 회원 리스트를 가져온다
     *
     * @return 회원 글 리스트
     */
    @GetMapping("/user")
    public ResponseEntity<ResponseBodyFormatter> adminUser() {

            /*Long user = 1L;


            if (user == null) {
                throw new CustomException(AdminErrorCode.USER_NOT_AUTHENTICATION);
            }*/


        return DataResponseBodyFormatter.put(AdminSuccessCode.PAGE_ALTER_SUCCESS, userService.userList());

    }

    /**
     * 관리자 페이지에서 모든 게임 글 리스트를 가져온다
     *
     * @return 게임 글 리스트
     */
    @GetMapping("/game")
    public ResponseEntity<ResponseBodyFormatter> adminGame() {

            /*Long user = 1L;

            if (user == null) {
                throw new CustomException(AdminErrorCode.USER_NOT_AUTHENTICATION);
            }*/

        return DataResponseBodyFormatter.put(AdminSuccessCode.PAGE_ALTER_SUCCESS, gameService.gameList());
    }

    /**
     * 관리자 페이지에서 모든 용병 글 리스트를 가져온다
     *
     * @param model
     * @return 용병 글 리스트
     */
    @GetMapping("/game/sub")
    public String adminSub(Model model) {
        model.addAttribute("subList", gameService.gameList());
        return "admin_sub";

    }

    /**
     * 통계 데이터를 가져와 보여준다
     *
     * @return 이번달 통계와 이번달의 하루 통계를 보여준다
     */
    @GetMapping("/stats")
    public ResponseEntity<ResponseBodyFormatter> getGameStats() {

            /*Long user = 1L;
            if (user == null) {
                throw new CustomException(AdminErrorCode.USER_NOT_AUTHENTICATION);
            }*/

        // 현재 날짜 가져오기
        LocalDate currentDate = LocalDate.now();

        StatsDTO monthStats = statsService.monthStats(currentDate);
        Map<LocalDate, StatsDTO> monthlyDailyStats = statsService.monthlyDailyStats(currentDate);


        return DataResponseBodyFormatter.put(
                AdminSuccessCode.PAGE_ALTER_SUCCESS,
                Map.of("monthStats", monthStats, "monthlyDailyStats", monthlyDailyStats)
        );
    }


    /**
     * 관리자 체크
     * userRole ==0 이면 에러
     * userRole==1 이면 성공
     * @return
     */
//        public ResponseEntity<ResponseBodyFormatter> isAdmin(){
//
//            Long user = 1L;
//
//            if (user == null) {
//                throw new CustomException(TeamErrorCode.USER_NOT_AUTHENTICATION);
//            } else if (!userService.) {
//                    throw new CustomException(TeamErrorCode.USER_NOT_AUTHENTICATION); 이안에 관리자 아니라는 에러메시지 넣기
//            }
//
//            return ResponseBodyFormatter.put(AdminSuccessCode.ADMIN_AUTH_SUCCESS);
//        }


}
