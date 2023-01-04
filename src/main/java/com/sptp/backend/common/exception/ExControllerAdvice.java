package com.sptp.backend.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class ExControllerAdvice {

    //일반 에러
    @ExceptionHandler
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        return ErrorResponse.toResponseEntity(e);
    }

    // api의 bean validation 관련 예외
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        FieldError fieldError = e.getFieldError();

        if (Objects.isNull(fieldError)) { // 일반적으로는 fieldError가 null이 될 수 없지만, 예외 케이스 처리 용도.
            return ErrorResponse.toResponseEntity(HttpStatus.BAD_REQUEST);
        }

        log.info(fieldError.toString());
        return ErrorResponse.toResponseEntity(HttpStatus.BAD_REQUEST, fieldError);
    }

}
