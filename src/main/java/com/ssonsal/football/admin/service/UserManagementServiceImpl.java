package com.ssonsal.football.admin.service;

import com.ssonsal.football.admin.dto.response.UserDTO;
import com.ssonsal.football.admin.exception.AdminErrorCode;
import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.global.util.ErrorCode;
import com.ssonsal.football.user.entity.User;
import com.ssonsal.football.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.ssonsal.football.admin.dto.response.UserDTO.userFactory;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagementService {

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

    @Override
    public List<UserDTO> userList() {
        List<User> userList = userRepository.findAll();

        return userList.stream()
                .map(user -> {
                    Integer calculatedAge = calculateAge(user.getId());
                    return userFactory(user, calculatedAge);
                })
                .collect(Collectors.toList());
    }


    @Transactional
    public void updateRoles(List<Integer> userIds) {
        userIds.stream()
                .map(userId -> userRepository.findById(Long.valueOf(userId))
                        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)))
                .filter(user -> user.getRole() != 1)
                .forEach(user -> {
                    int newRole = (user.getRole() == 0) ? 1 : 0;
                    user.updateRole(newRole);
                });
    }


    public boolean isAdmin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return user.getRole() != 1;
    }

}

