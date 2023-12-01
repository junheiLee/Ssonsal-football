package com.ssonsal.football.admin.service;

import com.ssonsal.football.admin.dto.request.UserDTO;
import com.ssonsal.football.admin.entity.User;
import com.ssonsal.football.admin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 유저 리스트
    public List<UserDTO> userList() {
        List<UserDTO> userList = userRepository.findAllUser();

        for (UserDTO user : userList) {
            user.setAge(userRepository.calculateAgeByUserId(user.getId()));
        }
        return userList;
    }

    // 유저 권한 변경
    public void updateRoles(List<Integer> userIds) {
        for (Integer userId : userIds) {
            Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));

            if (userOptional.isPresent()) {
                User user = userOptional.get();

                // 이미 관리자를 선택한 경우
                if (user.getRole() == 1) {
                    log.info("User {} is already an administrator. Skipping role update.", user.getId());
                    continue;
                }

                Integer newRole = (user.getRole() == 0) ? 1 : 0;
                user.setRole(newRole);
                userRepository.save(user);
            }
        }
    }
}