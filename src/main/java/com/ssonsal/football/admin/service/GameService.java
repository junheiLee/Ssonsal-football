package com.ssonsal.football.admin.service;

import com.ssonsal.football.admin.dto.request.GameDTO;
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

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GameService {

    private final GameManagementRepository gameManagementRepository;

    /**
    게임 글 리스트
    회원들이 작성한 글 정보 전체를 꺼내온다
    작성일자, 매치 상태, 대결 인원, 경기장, 작성자들을 관리자 페이지에서 볼 수 있다.
    매치가 확정 전인 리스트들만 보여준다.
    */
    @Transactional
    public List<GameDTO> gameList() {
        List<Game> games = gameManagementRepository.findAll();

        List<GameDTO> gameDTOs = games.stream()
                .filter(game -> game.getMatchStatus() == 0)
                .map(game -> GameDTO.builder()
                        .id(game.getId())
                        .writer(game.getWriter().getNickname())
                        .createdAt(game.getCreatedAt())
                        .matchStatus(game.getMatchStatus())
                        .vsFormat(game.getVsFormat())
                        .stadium(game.getStadium())
                        .build()
                )
                .collect(Collectors.toList());

        return gameDTOs;
    }
/**
    게임 글 삭제
    관리자 페이지에서 원하는 게임 글을 삭제 할 수있다.
    삭제가 실행되면 해당 글은 메인 사이트에서도 글이 지워진다
    request: gameIds는 체크된 게임 id들
    response: 게임 삭제
 */
    @Transactional
    public void deleteGames(List<Integer> gameIds) {
        gameIds.stream()
                .map(gameId -> Long.valueOf(gameId))
                .map(gameId -> gameManagementRepository.findById(gameId)
                        .orElseThrow(() -> new CustomException(AdminErrorCode.GAME_NOT_FOUND)))
                .forEach(game -> {
                    gameManagementRepository.deleteById(game.getId());
                });

    }
}