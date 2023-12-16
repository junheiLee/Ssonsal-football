package com.ssonsal.football.admin.service;

import com.ssonsal.football.admin.dto.request.UserDTO;
import com.ssonsal.football.admin.exception.AdminErrorCode;
import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.global.util.ErrorCode;
import com.ssonsal.football.global.util.formatter.DataResponseBodyFormatter;
import com.ssonsal.football.user.entity.User;
import com.ssonsal.football.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Integer calculateAge(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(AdminErrorCode.USER_NOT_FOUND));

        if (user == null || user.getBirth() == null) {
            return null;
        }

        LocalDate birth = user.getBirth();
        LocalDate currentDate = LocalDate.now();
        int age = currentDate.getYear() - birth.getYear();

        if (currentDate.getMonthValue() < birth.getMonthValue()
                || (currentDate.getMonthValue() == birth.getMonthValue()
                && currentDate.getDayOfMonth() < birth.getDayOfMonth())) {
            age--;
        }

        return age;

    }
   /**
    유저 리스트
    회원들의 정보 전체를 꺼내온다
    이름, 닉네임, 성별, 가입일자, 나이들을 관리자 페이지에서 볼 수 있다.
    */

    public List<UserDTO> userList() {
        List<User> userList = userRepository.findAll();

        List<UserDTO> userDTOs = userList.stream()
                .map(user -> {
                    Integer calculatedAge = calculateAge(user.getId());

                    return UserDTO.builder()
                            .id(user.getId())
                            .name(user.getName())
                            .nickname(user.getNickname())
                            .gender(user.getGender())
                            .createdAt(user.getCreatedAt())
                            .role(user.getRole())
                            .age(calculatedAge)  // 나이 설정
                            .build();
                })
                .collect(Collectors.toList());

        return userDTOs;
    }

    /**
    유저 권한 변경
    유저를 관리자로 변경해 주는 기능이다
    이미 관리자인 경우는 일반 회원으로 변경이 불가능하다
    request: userIds는 체크된 회원 id들
    response: role을 1로 변경
     */
    @Transactional
    public void updateRoles(List<Integer> userIds) {
        log.info("서비스");

        userIds.stream()
                .map(userId -> userRepository.findById(Long.valueOf(userId))
                        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)))
                .filter(user -> user.getRole() != 1)
                .forEach(user -> {
                    Integer newRole = (user.getRole() == 0) ? 1 : 0;
                    user.updateRole(newRole);
                });
        }
    }

