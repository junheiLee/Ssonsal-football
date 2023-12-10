package com.ssonsal.football.admin.controller;

import com.ssonsal.football.admin.dto.request.UpdateMonthDto;
import com.ssonsal.football.admin.exception.AdminErrorCode;
import com.ssonsal.football.admin.exception.AdminSuccessCode;
import com.ssonsal.football.admin.service.StatsService;
import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.global.util.formatter.ResponseBodyFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@Slf4j
@RequestMapping("/admin/stats")
@RequiredArgsConstructor
public class StatsRestController {

    private final StatsService statsService;

    /**
     * 관리자가 선택한 달의 통계 데이터를 보여준다
     * @param selectedDate
     * selectedDate는 관리자가 선택한 달로
     * 달이 선택되면 그 달의 한달 통계와 달의 하루씩 데이터를 보여준다
     * @return 성공코드와 한달 데이터, 하루 데이터를 반환
     */
    @PostMapping("/changeMonth")
    public ResponseEntity<ResponseBodyFormatter> updateMonth(@RequestBody UpdateMonthDto selectedDate) {

        Long user=1L;

        if (user == null) {
            throw new CustomException(AdminErrorCode.USER_NOT_AUTHENTICATION);
        }


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate currentDate = LocalDate.parse(selectedDate.getSelectedDate(), formatter);

/*            JSONObject json = new JSONObject(selectedDate);
            String dateValue = json.getString("selectedDate");
            LocalDate currentDate = LocalDate.parse(selectedDate);*/
        if (selectedDate.getSelectedDate() == null) {
            throw new CustomException(AdminErrorCode.MONTH_UPDATE_FAILED);
        }

            statsService.monthStats(currentDate);
            statsService.monthlyDailyStats(currentDate);

            return ResponseBodyFormatter.put(AdminSuccessCode.MONTH_UPDATE_SUCCESS);



    }
}