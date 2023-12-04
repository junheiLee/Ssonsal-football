package com.ssonsal.football.admin.controller;

import com.ssonsal.football.admin.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;


@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    // 해당 달 통계 데이터
    @GetMapping("/stats")
    public String getGameStats(Model model) {

        // 현재 날짜 가져오기
        LocalDate currentDate = LocalDate.now();

        model.addAttribute("statsDTO", statsService.monthStats(currentDate));
        model.addAttribute("dailyStatsMap", statsService.monthlyDailyStats(currentDate));

        return "game_stats";

    }
}
