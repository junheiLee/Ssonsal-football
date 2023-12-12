package com.ssonsal.football.admin.controller;

import com.ssonsal.football.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class UserManagementController {

    private final UserService userService;

    // 유저 리스트
    @GetMapping("/user")
    public String adminUser(Model model) {
        model.addAttribute("userList", userService.userList());

        log.info("유저!!" + userService.userList());

        return "admin_user";
    }

}
