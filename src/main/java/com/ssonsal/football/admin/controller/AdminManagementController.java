package com.ssonsal.football.admin.controller;

import com.ssonsal.football.admin.dto.request.UpdateMonthDto;
import com.ssonsal.football.admin.dto.response.StatsDTO;
import com.ssonsal.football.admin.exception.AdminErrorCode;
import com.ssonsal.football.admin.service.GameManagementService;
import com.ssonsal.football.admin.service.StatsService;
import com.ssonsal.football.admin.service.UserManagementService;
import com.ssonsal.football.global.account.Account;
import com.ssonsal.football.global.account.CurrentUser;
import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.global.util.SuccessCode;
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

import static com.ssonsal.football.admin.util.AdminConstant.*;
import static com.ssonsal.football.global.util.transfer.Transfer.toMap;

@RestController
@Slf4j
@RequestMapping("/api/management")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Admin API")
public class AdminManagementController {

    private final StatsService statsService;
    private final GameManagementService gameManagementService;
    private final UserManagementService userManagementService;

    /**
     * 관리자 권한 부여시 호출되는 api
     *
     * @param requestData requestData는 선택한 모든 회원 id를 가져온다
     * @return 성공 코드와 선택된 회원은 관리자로 변경된다
     */
    @PostMapping("/user/changeRole")
    public ResponseEntity<ResponseBodyFormatter> updateUserRole(@RequestBody Map<String, Object> requestData, @CurrentUser Account account) {

        List<Integer> userIds = (List<Integer>) requestData.get("userIds");
        Long userId = account.getId();

        if (userManagementService.isAdmin(userId)) {
            throw new CustomException(AdminErrorCode.ADMIN_AUTH_FAILED);
        } else if (userIds == null) {
            throw new CustomException(AdminErrorCode.USER_SELECTED_FAILED);
        }


        userManagementService.updateRoles(userIds);
        return DataResponseBodyFormatter.put(SuccessCode.SUCCESS, toMap(RECOGNIZE_ADMIN, userIds));

    }

    /**
     * 게임 글 삭제 요청시 호출되는 api
     *
     * @param requestData requestData는 선택한 모든 글 id를 가져온다
     * @return 성공 코드와 게임 글이 삭제된다
     */
    @PostMapping("/game/management")
    public ResponseEntity<ResponseBodyFormatter> deleteGames(@RequestBody Map<String, Object> requestData, @CurrentUser Account account) {

        List<Integer> gameIds = (List<Integer>) requestData.get("gameIds");
        Long userId = account.getId();

        if (userManagementService.isAdmin(userId)) {
            throw new CustomException(AdminErrorCode.ADMIN_AUTH_FAILED);
        } else if (gameIds == null) {
            throw new CustomException(AdminErrorCode.GAME_NOT_FOUND);
        }

        gameManagementService.deleteGames(gameIds);

        return DataResponseBodyFormatter.put(SuccessCode.SUCCESS, toMap(DELETE_POST, gameIds));

    }


    /**
     * 관리자가 선택한 달의 통계 데이터를 보여준다
     *
     * @param selectedDate selectedDate는 관리자가 선택한 달로
     *                     달이 선택되면 그 달의 한달 통계와 달의 하루씩 데이터를 보여준다
     * @return 성공코드와 한달 데이터, 하루 데이터를 반환
     */
    @PostMapping("/stats/changeMonth")
    public ResponseEntity<ResponseBodyFormatter> updateMonth(@RequestBody UpdateMonthDto selectedDate, @CurrentUser Account account) {

        Long userId = account.getId();

        if (userManagementService.isAdmin(userId)) {
            throw new CustomException(AdminErrorCode.ADMIN_AUTH_FAILED);
        } else if (selectedDate.getSelectedDate() == null) {
            throw new CustomException(AdminErrorCode.MONTH_UPDATE_FAILED);
        }

        try {

            LocalDateTime selectedDateTime = LocalDateTime.parse(selectedDate.getSelectedDate(), DateTimeFormatter.ISO_DATE_TIME);
            LocalDate currentDate = selectedDateTime.toLocalDate();

            StatsDTO monthStats = statsService.monthStats(currentDate);
            Map<LocalDate, StatsDTO> monthlyDailyStats = statsService.monthlyDailyStats(currentDate);

            return DataResponseBodyFormatter.put(
                    SuccessCode.SUCCESS,
                    Map.of(MONTH_STATS, monthStats, MONTHLY_DAILY_STATS, monthlyDailyStats));
        } catch (DateTimeParseException e) {
            throw new CustomException(AdminErrorCode.INVALID_DATE_FORMAT);
        }
    }
}