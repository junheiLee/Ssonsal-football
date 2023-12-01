package com.ssonsal.football.admin.controller;

import com.ssonsal.football.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class UserManagementController {

    private final UserService userService;

    // 유저 리스트
    @GetMapping("/user")
    public String adminUser(Model model) {
        model.addAttribute("userList", userService.userList());

        return "admin_user";
    }

}
