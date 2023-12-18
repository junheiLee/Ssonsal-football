package com.ssonsal.football.rank.controller;

import com.ssonsal.football.global.util.formatter.DataResponseBodyFormatter;
import com.ssonsal.football.global.util.formatter.ResponseBodyFormatter;
import com.ssonsal.football.rank.service.RankService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.ssonsal.football.global.util.SuccessCode.SUCCESS;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/ranks")
public class RankController {

    private final RankService rankService;

    @GetMapping
    public ResponseEntity<ResponseBodyFormatter> ranks(HttpServletRequest request) {

        //        Long user = jwtTokenProvider.getUserId(request.getHeader("ssonToken"));

        return DataResponseBodyFormatter.put(SUCCESS, rankService.findRankList());
    }

    @PostMapping
    public ResponseEntity<ResponseBodyFormatter> updateRanks(HttpServletRequest request) {

        //        Long user = jwtTokenProvider.getUserId(request.getHeader("ssonToken"));

        return DataResponseBodyFormatter.put(SUCCESS, rankService.updateRank());
    }
}
