package com.ssonsal.football.admin.controller;

import com.ssonsal.football.admin.exception.AdminErrorCode;
import com.ssonsal.football.admin.exception.AdminSuccessCode;
import com.ssonsal.football.admin.service.GameService;
import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.global.util.formatter.DataResponseBodyFormatter;
import com.ssonsal.football.global.util.formatter.ResponseBodyFormatter;
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
public class GameRestController {

    private final GameService gameService;

    /**
     * 게임 글 삭제 요청시 호출되는 api
     *
     * @param requestData requestData는 선택한 모든 글 id를 가져온다
     * @return 성공 코드와 게임 글이 삭제된다
     */
    @PostMapping("/management")
    public ResponseEntity<ResponseBodyFormatter> deleteGames(@RequestBody Map<String, Object> requestData) {
        List<Integer> gameIds = (List<Integer>) requestData.get("gameIds");

        Long user = 1L;

            if (user == null) {
                throw new CustomException(AdminErrorCode.USER_NOT_AUTHENTICATION);
            } else if(gameIds == null) {
                throw new CustomException(AdminErrorCode.GAME_NOT_FOUND);
            }

            gameService.deleteGames(gameIds);

            return ResponseBodyFormatter.put(AdminSuccessCode.GAME_POST_DELETED);


    }
}