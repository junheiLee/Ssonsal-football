package com.ssonsal.football.game.service;


import com.ssonsal.football.game.dto.request.CreateGameRequestDto;
import com.ssonsal.football.game.dto.request.EnterResultRequestDto;
import com.ssonsal.football.game.dto.response.GameInfoResponseDto;
import com.ssonsal.football.game.dto.response.GameListResponseDto;
import com.ssonsal.football.game.dto.response.GameResultResponseDto;

import java.util.List;

public interface GameService {

    /**
     * 게임 생성 시, DB에 게임 정보를 기입하는 기능
     *
     * @param loginUserId   현재 로그인 중인 회원의 식별자
     * @param createGameDto 입력한 게임 정보 및 게임 참여 정보
     * @return 생성된 게임 식별자
     */
    Long insertGame(Long loginUserId, CreateGameRequestDto createGameDto);

    /**
     * 게임 정보를 찾아 반환하는 기능
     *
     * @param gameId 해당하는 게임 식별자
     * @return 게임 관련 정보
     */
    GameInfoResponseDto findGameInfo(Long gameId);

    /**
     * 게임 결과를 기입하는 기능
     *
     * @param loginUserId   로그인한 회원의 식별자
     * @param gameId        결과를 기입하려 하는 대상 게임 아이디
     * @param gameResultDto 기입하는 결과와 타겟 팀 정보
     * @return 해당 하는 팀의 결과 기입 후, DB에 저장된 게임 결과 정보 반환
     */
    GameResultResponseDto enterResult(Long loginUserId, Long gameId, EnterResultRequestDto gameResultDto);

    /**
     * 모든 게임을 반환하는 기능
     *
     * @return 모든 게임 목록
     */
    List<GameListResponseDto> findAllGames();

    /**
     * 팀을 구하고 있는 게임을 반환하는 기능
     *
     * @return 팀을 구하고 있는 모든 게임
     */
    List<GameListResponseDto> findAllGamesForTeam();

    /**
     * 용병을 구하고 있는 게임을 반환하는 기능
     *
     * @return 용병을 구하고 있는 모든 게임
     */
    List<GameListResponseDto> findAllGamesForSub();

    /**
     * 해당 회원이 용병으로 참여한 게임을 반환하는 기능
     *
     * @param userId 해당 회원의 식별자
     * @return 해당 회원이 용병으로 참여한 모든 게임
     */
    List<GameListResponseDto> findGamesBySub(Long userId);

    /**
     * 해당 팀이 참여한 게임을 반환하는 기능
     *
     * @param teamId 해당 팀의 식별자
     * @return 해당 팀이 참여한 모든 게임
     */
    List<GameListResponseDto> findGamesByTeam(Long teamId);

}
