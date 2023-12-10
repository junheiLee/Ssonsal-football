package com.ssonsal.football.admin.controller;

import com.ssonsal.football.admin.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class AdminExceptionHandler {

    /**
     * login이 안 되었을 시 login 화면으로 넘어간다
     *
     * @param ex
     * @param model
     * @return login 화면으로 이동
     */
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundUser(NotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "login";
    }
}
