package com.ssonsal.football.admin.controller;

import com.ssonsal.football.admin.dto.request.UpdateMonthDto;
import com.ssonsal.football.admin.service.StatsService;
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
public class RestStatsController {

    private final StatsService statsService;

    // 월 통계 데이터 변환
    @PostMapping("/changeMonth")
    public ResponseEntity<String> updateMonth(@RequestBody UpdateMonthDto selectedDate) {
        log.info("date =={}", selectedDate.toString());
        try {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate currentDate = LocalDate.parse(selectedDate.getSelectedDate(), formatter);

/*            JSONObject json = new JSONObject(selectedDate);
            String dateValue = json.getString("selectedDate");
            LocalDate currentDate = LocalDate.parse(selectedDate);*/

            statsService.monthStats(currentDate);


            statsService.monthlyDailyStats(currentDate);

            return ResponseEntity.ok("업데이트 성공");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류: " + e.getMessage());
        }
    }
}