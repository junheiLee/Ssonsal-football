package com.ssonsal.football.game.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ssonsal.football.game.dto.request.GameRequestDto;
import com.ssonsal.football.game.dto.request.MatchApplicationRequestDto;
import com.ssonsal.football.game.service.GameService;
import com.ssonsal.football.game.util.Transfer;
import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.global.util.ErrorCode;
import com.ssonsal.football.global.util.formatter.DataResponseBodyFormatter;
import com.ssonsal.football.global.util.formatter.ResponseBodyFormatter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.ssonsal.football.global.util.SuccessCode.SUCCESS;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/games")
@Tag(name = "Game", description = "Game API")
public class GameController {

    private final GameService gameService;

    /**
     * 게임 생성 시, 호출되는 api
     *
     * @param obj 게임 생성에 필요한 정보
     * @return 성공 코드와 생성된 게임 아이디를 ResponseBody에 담아 반환
     */
    @PostMapping
    public ResponseEntity<ResponseBodyFormatter> createGame(@RequestBody ObjectNode obj) {

        Long userId = 3L;
        Map<String, Long> createGameResponseDto;

        ObjectMapper mapper = new ObjectMapper();
        try {
            GameRequestDto gameDto
                    = mapper.treeToValue(obj.get("game"), GameRequestDto.class);
            MatchApplicationRequestDto homeTeamDto
                    = mapper.treeToValue(obj.get("hometeam"), MatchApplicationRequestDto.class);

            Long gameId = gameService.createGame(userId, gameDto, homeTeamDto);
            createGameResponseDto = Transfer.longIdToMap("gameId", gameId);

        } catch (JsonProcessingException e) {
            log.error("Request Body의 형식이 다릅니다.");
            throw new CustomException(e, ErrorCode.WRONG_FORMAT);
        }

        return DataResponseBodyFormatter.put(SUCCESS, createGameResponseDto);
    }

    @GetMapping("/for-team")
    public ResponseEntity<ResponseBodyFormatter> gamesForTeam() {

        return DataResponseBodyFormatter.put(SUCCESS, gameService.findAllGamesForTeam());
    }

    @GetMapping("/for-sub")
    public ResponseEntity<ResponseBodyFormatter> gamesForSub() {

        return DataResponseBodyFormatter.put(SUCCESS, gameService.findAllGamesForSub());
    }

}
