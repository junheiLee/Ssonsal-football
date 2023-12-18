package com.ssonsal.football.admin.controller;

import com.ssonsal.football.admin.dto.request.StatsDTO;
import com.ssonsal.football.admin.dto.request.UpdateMonthDto;
import com.ssonsal.football.admin.exception.AdminErrorCode;
import com.ssonsal.football.admin.exception.AdminSuccessCode;
import com.ssonsal.football.admin.service.GameService;
import com.ssonsal.football.admin.service.StatsService;
import com.ssonsal.football.admin.service.UserService;
import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.global.util.formatter.DataResponseBodyFormatter;
import com.ssonsal.football.global.util.formatter.ResponseBodyFormatter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Admin API")
public class AdminManagementController {

    private final StatsService statsService;
    private final GameService gameService;
    private final UserService userService;

    /**
     * 관리자 권한 부여시 호출되는 api
     *
     * @param requestData requestData는 선택한 모든 회원 id를 가져온다
     * @return 성공 코드와 선택된 회원은 관리자로 변경된다
     */
    @PostMapping("/user/changeRole")
    public ResponseEntity<ResponseBodyFormatter> updateUserRole(@RequestBody Map<String, Object> requestData) {

        List<Integer> userIds = (List<Integer>) requestData.get("userIds");
        Long user = 1L;


        if (user == null) {
            throw new CustomException(AdminErrorCode.USER_NOT_AUTHENTICATION);
        } else if (userIds == null) {
            throw new CustomException(AdminErrorCode.USER_SELECTED_FAILED);
        }

        userService.updateRoles(userIds);
        return DataResponseBodyFormatter.put(AdminSuccessCode.USER_ALTER_ROLE, userIds);

    }

    /**
     * 게임 글 삭제 요청시 호출되는 api
     *
     * @param requestData requestData는 선택한 모든 글 id를 가져온다
     * @return 성공 코드와 게임 글이 삭제된다
     */
    @PostMapping("/game/management")
    public ResponseEntity<ResponseBodyFormatter> deleteGames(@RequestBody Map<String, Object> requestData) {
        List<Integer> gameIds = (List<Integer>) requestData.get("gameIds");

        Long user = 1L;

        if (user == null) {
            throw new CustomException(AdminErrorCode.USER_NOT_AUTHENTICATION);
        } else if (gameIds == null) {
            throw new CustomException(AdminErrorCode.GAME_NOT_FOUND);
        }

        gameService.deleteGames(gameIds);

        return ResponseBodyFormatter.put(AdminSuccessCode.GAME_POST_DELETED);

    }


    /**
     * 관리자가 선택한 달의 통계 데이터를 보여준다
     *
     * @param selectedDate selectedDate는 관리자가 선택한 달로
     *                     달이 선택되면 그 달의 한달 통계와 달의 하루씩 데이터를 보여준다
     * @return 성공코드와 한달 데이터, 하루 데이터를 반환
     */
    @PostMapping("/stats/changeMonth")
    public ResponseEntity<ResponseBodyFormatter> updateMonth(@RequestBody UpdateMonthDto selectedDate) {
        log.info(selectedDate + " 날짜데이터");

        Long user = 1L;

        if (user == null) {
            throw new CustomException(AdminErrorCode.USER_NOT_AUTHENTICATION);
        }

        if (selectedDate.getSelectedDate() == null) {
            throw new CustomException(AdminErrorCode.MONTH_UPDATE_FAILED);
        }

        try {

            LocalDateTime selectedDateTime = LocalDateTime.parse(selectedDate.getSelectedDate(), DateTimeFormatter.ISO_DATE_TIME);

            log.info("aaaaaa" + selectedDateTime);

            LocalDate currentDate = selectedDateTime.toLocalDate();
            log.info("bbb" + currentDate);

            // 이후에는 필요에 따라 로직을 처리합니다.
            StatsDTO monthStats =statsService.monthStats(currentDate);
            Map<LocalDate, StatsDTO>  monthlyDailyStats = statsService.monthlyDailyStats(currentDate);

            log.info("서비스"+statsService.monthlyDailyStats(currentDate));
            log.info("서비스"+statsService.monthStats(currentDate));

            log.info(monthlyDailyStats+"날짜데이터");

            log.info(currentDate + " 일별 데이터");

            return DataResponseBodyFormatter.put(
                    AdminSuccessCode.PAGE_ALTER_SUCCESS,
                    Map.of("monthStats", monthStats, "monthlyDailyStats", monthlyDailyStats)
            );
        } catch (DateTimeParseException e) {
            // 파싱에 실패한 경우 예외 처리
            log.error("날짜 파싱에 실패했습니다.", e);
            throw new CustomException(AdminErrorCode.INVALID_DATE_FORMAT);
        }
    }
}