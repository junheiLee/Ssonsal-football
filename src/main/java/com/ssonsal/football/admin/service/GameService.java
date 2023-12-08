package com.ssonsal.football.admin.service;

import com.ssonsal.football.admin.dto.request.GameDTO;
import com.ssonsal.football.admin.repository.GameManagementRepository;
import com.ssonsal.football.game.entity.Game;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GameService {

    private final GameManagementRepository gameManagementRepository;

    // 게임 글 리스트
    @Transactional

        public List<GameDTO> gameList() {

        List<Game> games = gameManagementRepository.findAll();
        List<GameDTO> gameDTOs = new ArrayList<>();

        /*List<GameDTO> game = gameManagementRepository.findAllGame();
        for (GameDTO gameDTO : game) {
            gameDTO.setWriter("홍길동");
        }
        return game;*/
        for (Game game : games) {
            GameDTO gameDTO = GameDTO.builder()
                    .id(game.getId())
                    .createdAt(game.getCreatedAt())
                    .matchStatus(game.getMatchStatus())
                    .vsFormat(game.getVsFormat())
                    .stadium(game.getStadium())
                    .build();
            gameDTOs.add(gameDTO);
        }

        return gameDTOs;
    }

    // 게임 글 삭제
    @Transactional
    public void deleteGames(List<Integer> gameIds) {
        for (Integer gameId : gameIds) {

            gameManagementRepository.deleteById(Long.valueOf(gameId));

            log.info("게임 ID {}가 삭제되었습니다.", gameId);
        }
    }
}