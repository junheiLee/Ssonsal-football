package com.ssonsal.football.admin.service;

import com.ssonsal.football.admin.dto.request.GameDTO;

import java.util.List;

public interface GameManagementService {

    /**
     게임 글 리스트
     회원들이 작성한 글 정보 전체를 꺼내온다
     작성일자, 매치 상태, 대결 인원, 경기장, 작성자들을 관리자 페이지에서 볼 수 있다.
     매치가 확정 전인 리스트들만 보여준다.
     */
    List<GameDTO> gameList();

    /**
     게임 글 삭제
     관리자 페이지에서 원하는 게임 글을 삭제 할 수있다.
     삭제가 실행되면 해당 글은 메인 사이트에서도 글이 지워진다
     request: gameIds는 체크된 게임 id들
     response: 게임 삭제
     */
    void deleteGames(List<Integer> gameIds);
}
