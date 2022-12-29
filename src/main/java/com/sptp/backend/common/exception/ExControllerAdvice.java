package com.sptp.backend.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExControllerAdvice {

    //일반 에러
    @ExceptionHandler
    protected ResponseEntity<Object> handleCustomException(CustomException e) {
        return ErrorResponse.toResponseEntity(e);
    }



}
