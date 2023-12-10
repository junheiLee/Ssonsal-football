package com.ssonsal.football.rank.controller;

import com.ssonsal.football.global.util.formatter.ResponseBodyFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/ranks")
public class RankController {

    @GetMapping
    public ResponseEntity<ResponseBodyFormatter> ranks() {
        return null;
    }

    @PostMapping
    public ResponseEntity<ResponseBodyFormatter> updateRanks() {
        return null;
    }
}
