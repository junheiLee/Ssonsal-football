package com.ssonsal.football.global.exception;

import com.ssonsal.football.global.util.ResponseCode;

import java.nio.file.AccessDeniedException;

public class CustomAccessDeniedException extends AccessDeniedException {
    public CustomAccessDeniedException(String error){
        super(error);
    }
}
