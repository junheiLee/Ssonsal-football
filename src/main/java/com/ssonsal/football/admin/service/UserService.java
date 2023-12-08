package com.ssonsal.football.admin.service;

import com.ssonsal.football.admin.dto.request.EmailDTO;
import com.ssonsal.football.admin.dto.request.MessageDTO;
import com.ssonsal.football.admin.dto.request.UserDTO;
import com.ssonsal.football.admin.repository.UserManagementRepository;
import com.ssonsal.football.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserManagementRepository userManagementRepository;

    @Transactional
    public  EmailDTO emailService(String emailText) {
        EmailDTO emailDTO = new EmailDTO();

        emailDTO.setText("안녕하세요, 사용자님! " + emailText);

        log.info("이메일 내용"+emailDTO.getText());
        log.info("DTO 는 "+emailDTO);

        return emailDTO;
    }


    // 유저 리스트
    public List<UserDTO> userList() {
        List<UserDTO> userList = userManagementRepository.findAllUser();

        for (UserDTO user : userList) {
            user.setAge(userManagementRepository.calculateAgeByUserId(user.getId()));
        }
        return userList;
    }

    @Transactional
    // 유저 권한 변경
    public void updateRoles(List<Integer> userIds) {
        for (Integer userId : userIds) {
            Optional<User> userOptional = userManagementRepository.findById(Long.valueOf(userId));

            if (userOptional.isPresent()) {
                User user = userOptional.get();

                // 이미 관리자를 선택한 경우
                if (user.getRole() == 1) {
                    log.info("유저가 이미 관리자인 경우", user.getId());
                    continue;
                }

                Integer newRole = (user.getRole() == 0) ? 1 : 0;
                user.updateRole(newRole);
            }
        }
    }
@Transactional
    public List<EmailDTO> memberList() {
        List<EmailDTO> memberList = userManagementRepository.findAllMember();

        return memberList;
    }

}