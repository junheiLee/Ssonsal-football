package com.ssonsal.football.admin.controller;

import com.ssonsal.football.admin.exception.AdminErrorCode;
import com.ssonsal.football.admin.exception.NotFoundException;
import com.ssonsal.football.admin.service.UserService;
import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.global.util.ErrorCode;
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

    /**
     * 관리자 페이지에서 모든 회원 리스트를 가져온다
     *
     * @param model
     * @return 회원 글 리스트
     */
    @GetMapping("/user")
    public String adminUser(Model model) {

        Long user = 1L;


            if (user == null) {
                throw new CustomException(AdminErrorCode.USER_NOT_AUTHENTICATION);
            }
            model.addAttribute("userList", userService.userList());

            return "admin_user";

        }

    }

