package com.ssonsal.football.game.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ssonsal.football.game.dto.request.GameRequestDto;
import com.ssonsal.football.game.dto.request.MatchApplicationRequestDto;
import com.ssonsal.football.game.service.GameService;
import com.ssonsal.football.global.util.SuccessCode;
import com.ssonsal.football.global.util.formatter.DataResponseBodyFormatter;
import com.ssonsal.football.global.util.formatter.ResponseBodyFormatter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

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
     * @throws JsonProcessingException: ObjectMapper를 사용해서 생김
     * @throws ParseException:          Service 계층에서 String을 LocalDateTime으로 변환할 때 생김
     */
    @PostMapping
    public ResponseEntity<ResponseBodyFormatter> createGame(@RequestBody ObjectNode obj) throws JsonProcessingException, ParseException {

        Long userId = 3L;
        ObjectMapper mapper = new ObjectMapper();
        GameRequestDto gameDto = mapper.treeToValue(obj.get("game"), GameRequestDto.class);
        MatchApplicationRequestDto homeTeamDto = mapper.treeToValue(obj.get("hometeam"), MatchApplicationRequestDto.class);

        Long gameId = gameService.createGame(userId, gameDto, homeTeamDto);

        return DataResponseBodyFormatter.put(SuccessCode.SUCCESS, dataToMap("gameId", gameId));
    }

    private Map<String, Long> dataToMap(String key, Long value) {

        Map<String, Long> dataDto = new HashMap<>();
        dataDto.put(key, value);
        return dataDto;
    }


}
