package com.ssonsal.football.rank.controller;

import com.ssonsal.football.admin.exception.AdminErrorCode;
import com.ssonsal.football.admin.service.UserManagementService;
import com.ssonsal.football.global.account.Account;
import com.ssonsal.football.global.account.CurrentUser;
import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.global.util.formatter.DataResponseBodyFormatter;
import com.ssonsal.football.global.util.formatter.ResponseBodyFormatter;
import com.ssonsal.football.rank.dto.MonthRequest;
import com.ssonsal.football.rank.dto.UpdatedRankDto;
import com.ssonsal.football.rank.service.RankService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ssonsal.football.global.util.SuccessCode.SUCCESS;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/ranks")
public class RankController {

    private final RankService rankService;
    private final UserManagementService userManagementService;

    @GetMapping("/list")
    public ResponseEntity<ResponseBodyFormatter> getRanks(@CurrentUser Account account,
                                                          @RequestParam(required = false) Integer month) {
        Long user = account.getId();

        return DataResponseBodyFormatter.put(SUCCESS, rankService.findRankList(month));
    }

        @PostMapping("/changeMonth")
        public ResponseEntity<ResponseBodyFormatter> getRanksByMonth(@CurrentUser Account account,
                                                                     @RequestBody(required = false)  Integer month) {
        Long user = account.getId();

        return DataResponseBodyFormatter.put(SUCCESS, rankService.findRankList(month));
    }

    @GetMapping("/reset")
    public ResponseEntity<ResponseBodyFormatter> updateRanks(@CurrentUser Account account) {

        log.info("리셋 시작");
        Long user = account.getId();

        return DataResponseBodyFormatter.put(SUCCESS, rankService.updateRank());
    }
}
