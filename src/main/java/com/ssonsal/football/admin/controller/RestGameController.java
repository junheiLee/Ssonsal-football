package com.ssonsal.football.admin.controller;

import com.ssonsal.football.admin.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/admin/game")
@RequiredArgsConstructor
public class RestGameController {

    private final GameService gameService;


    // 게임 삭제
    @PostMapping("/management")
    public ResponseEntity<String> deleteGames(@RequestBody Map<String, Object> reqeustData) {
        List<Integer> gameIds = (List<Integer>) reqeustData.get("gameIds");

        log.info("게임 아이디" + gameIds);

        try {
            gameService.deleteGames(gameIds);

            return ResponseEntity.ok("삭제 성공");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 실패");
        }
    }
}
