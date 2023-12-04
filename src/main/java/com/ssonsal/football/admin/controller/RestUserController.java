package com.ssonsal.football.admin.controller;

import com.ssonsal.football.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class RestUserController {


    private final UserService userService;

    // 유저 권한 부여
    @PostMapping("/changeRole")
    public ResponseEntity<String> updateUserRole(@RequestBody Map<String, Object> requestData) {
        List<Integer> userIds = (List<Integer>) requestData.get("userIds");

        try {
            userService.updateRoles(userIds);

            return ResponseEntity.ok("권한 부여 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("권한 부여 실패");
        }
    }
}
