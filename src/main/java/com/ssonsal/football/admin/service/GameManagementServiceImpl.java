package com.ssonsal.football.admin.service;

import com.ssonsal.football.admin.dto.response.GameDTO;
import com.ssonsal.football.admin.exception.AdminErrorCode;
import com.ssonsal.football.admin.repository.GameManagementRepository;
import com.ssonsal.football.game.entity.Game;
import com.ssonsal.football.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.ssonsal.football.admin.dto.response.GameDTO.gameFactory;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GameManagementServiceImpl implements GameManagementService {

    private final GameManagementRepository gameManagementRepository;

    @Override
    public List<GameDTO> gameList() {
        List<Game> games = gameManagementRepository.findAll();

        return games.stream()
                .filter(game -> game.getMatchStatus() == 0)
                .map(game -> gameFactory(game))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteGames(List<Integer> gameIds) {
        gameIds.stream()
                .map(gameId -> Long.valueOf(gameId))
                .map(gameId -> gameManagementRepository.findById(gameId)
                        .orElseThrow(() -> new CustomException(AdminErrorCode.GAME_NOT_FOUND)))
                .forEach(game -> gameManagementRepository.deleteById(game.getId()));

    }
}