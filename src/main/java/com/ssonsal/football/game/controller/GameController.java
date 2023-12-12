package com.ssonsal.football.game.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ssonsal.football.game.dto.request.GameRequestDto;
import com.ssonsal.football.game.dto.request.MatchApplicationRequestDto;
import com.ssonsal.football.game.dto.response.GameDetailResponseDto;
import com.ssonsal.football.game.service.GameService;
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

import static com.ssonsal.football.game.util.GameConstant.*;
import static com.ssonsal.football.game.util.Transfer.longIdToMap;
import static com.ssonsal.football.game.util.Transfer.toMapIncludeUserInfo;
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
     * @return 성공 코드와 생성된 게임 아이디를 ResponseBody 에 담아 반환
     */
    @PostMapping
    public ResponseEntity<ResponseBodyFormatter> createGame(@RequestBody ObjectNode obj) {

        Long userId = 3L;
        Map<String, Long> createGameResponseDto;

        ObjectMapper mapper = new ObjectMapper();
        try {
            GameRequestDto gameDto
                    = mapper.treeToValue(obj.get(GAME), GameRequestDto.class);
            MatchApplicationRequestDto homeTeamDto
                    = mapper.treeToValue(obj.get(HOME), MatchApplicationRequestDto.class);

            Long gameId = gameService.createGame(userId, gameDto, homeTeamDto);
            createGameResponseDto = longIdToMap("createdGameId", gameId);

        } catch (JsonProcessingException e) {
            log.error("찾을 수 없는 key : value 입니다.");
            throw new CustomException(e, ErrorCode.WRONG_JSON_FORMAT);
        }

        return DataResponseBodyFormatter.put(SUCCESS, createGameResponseDto);
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<ResponseBodyFormatter> detail(@PathVariable Long gameId) {

        Long userId = 2L;
        Long teamId = null;
        GameDetailResponseDto gameDetailResponseDto = gameService.getDetail(gameId);

        return DataResponseBodyFormatter.put(SUCCESS, toMapIncludeUserInfo(userId, teamId, GAMES, gameDetailResponseDto));
    }


    /*
    수정 로직은 match status 에 따라 수정/삭제 가능 여부가 정해진 후 확정할 수 있음.
     */
    //@PutMapping("{gameId}")
    @Deprecated
    public ResponseEntity<ResponseBodyFormatter> updateGame(@PathVariable Long gameId, @RequestBody ObjectNode obj) {

        Long userId = 3L;
        Map<String, Long> updateGameResponseDto;

        ObjectMapper mapper = new ObjectMapper();
        try {
            GameRequestDto updateGameDto
                    = mapper.treeToValue(obj.get(GAME), GameRequestDto.class);
            MatchApplicationRequestDto updateHomeTeamDto
                    = mapper.treeToValue(obj.get(HOME), MatchApplicationRequestDto.class);

            Long updatedGameId = gameService.updateGame(userId, gameId, updateGameDto, updateHomeTeamDto);
            updateGameResponseDto = longIdToMap("updatedGameId", updatedGameId);

        } catch (JsonProcessingException e) {
            log.error("Request Body 의 형식이 다릅니다.");
            throw new CustomException(e, ErrorCode.WRONG_JSON_FORMAT);
        }

        return DataResponseBodyFormatter.put(SUCCESS, updateGameResponseDto);
    }


    /**
     * 팀을 구하고 있는 게임 글 목록을 반환하는 api
     *
     * @return 상대 팀을 구하고 있는 게임 타이틀 정보 list 반환
     */
    @GetMapping("/for-team")
    public ResponseEntity<ResponseBodyFormatter> gamesForTeam() {

        return DataResponseBodyFormatter.put(SUCCESS, gameService.findAllGamesForTeam());
    }

    /**
     * 용병을 구하고 있는 게임 글 목록을 반환하는 api
     *
     * @return 용병을 구하고 있는 게임 타이틀 정보 list 반환
     */
    @GetMapping("/for-sub")
    public ResponseEntity<ResponseBodyFormatter> gamesForSub() {

        return DataResponseBodyFormatter.put(SUCCESS, gameService.findAllGamesForSub());
    }

    /**
     * 해당 유저가 용병으로 참여한 게임 글 목록 반환 api
     *
     * @param userId 유저 아이디
     * @return 해당 유저가 용병으로 참여한 게임 타이틀 정보 list 반환
     */
    @GetMapping("/subs/{userId}")
    public ResponseEntity<ResponseBodyFormatter> myGamesAsSub(@PathVariable Long userId) {

        return DataResponseBodyFormatter.put(SUCCESS, gameService.findMyGamesAsSub(userId));
    }

    /**
     * 해당 팀이 참여한 게임 글 목록 반환 api
     *
     * @param teamId 해당 팀 아이디
     * @return 해당 팀이 참여한 게임 타이틀 정보 list 반환
     */
    @GetMapping("/teams/{teamId}")
    public ResponseEntity<ResponseBodyFormatter> ourGamesAsTeam(@PathVariable Long teamId) {

        return DataResponseBodyFormatter.put(SUCCESS, gameService.findOurGamesAsTeam(teamId));
    }

}
