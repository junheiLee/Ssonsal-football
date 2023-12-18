package com.ssonsal.football.admin.service;

import com.ssonsal.football.admin.dto.response.UserDTO;

import java.util.List;

public interface UserManagementService {

    /**
     * 나이 구하는 로직
     * 출생년도를 통해 나이를 구하는 로직
     * 현재 년도에서 출생 년도를 빼서 만 나이로 계산
     * 생일이 지나지 않은 사람도 현재 시간을 비교해 지나지 않으면 -1(만 나이)
     *
     * @param userId
     * @return
     */
    Integer calculateAge(Long userId);

    /**
     * 유저 리스트
     * 회원들의 정보 전체를 꺼내온다
     * 이름, 닉네임, 성별, 가입일자, 나이들을 관리자 페이지에서 볼 수 있다.
     */
    List<UserDTO> userList();

    /**
     * 유저 권한 변경
     * 유저를 관리자로 변경해 주는 기능이다
     * 이미 관리자인 경우는 일반 회원으로 변경이 불가능하다
     * request: userIds는 체크된 회원 id들
     * response: role을 1로 변경
     */
    void updateRoles(List<Integer> userIds);

    /**
     * 관리자 체크 로직
     * Role==0 이면 일반 유저
     * Role==1 이면 관리자
     * Role==1인 유저만 관리자 페이지에서 로그인 가능
     *
     * @param userId
     * @return 관리자 확인 체크
     */
    public boolean isAdmin(Long userId);

}
