package com.sptp.backend.exception;

import com.sptp.backend.common.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExControllerAdvice {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> UserExHandle(UsernameNotFoundException e) {
        log.info("[exceptionHandle] {}", e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> ArgsExHandle(IllegalArgumentException e) {
        log.info("[exceptionHandle] {}", e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.fail(e.getMessage()));
    }

}
