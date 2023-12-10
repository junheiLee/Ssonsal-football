package com.ssonsal.football.admin.controller;

import com.ssonsal.football.admin.exception.AdminErrorCode;
import com.ssonsal.football.admin.exception.NotFoundException;
import com.ssonsal.football.admin.service.StatsService;
import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.global.util.ErrorCode;
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

    /**
     *  통계 데이터를 가져와 보여준다
     * @param model
     * @return 이번달 통계와 이번달의 하루 통계를 보여준다
     */
    @GetMapping("/stats")
    public String getGameStats(Model model) {

        Long user = 1L;


            if (user == null) {
                throw new CustomException(AdminErrorCode.USER_NOT_AUTHENTICATION);
            }

            // 현재 날짜 가져오기
            LocalDate currentDate = LocalDate.now();

            model.addAttribute("statsDTO", statsService.monthStats(currentDate));
            model.addAttribute("dailyStatsMap", statsService.monthlyDailyStats(currentDate));

            return "game_stats";

        }

    }

