package com.sptp.backend.common.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String error;
    private final String code;
    private final String detail;

    public static ResponseEntity<ErrorResponse> toResponseEntity(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(
                        ErrorResponse.builder()
                                .status(errorCode.getHttpStatus().value())//httpStatus 코드
                                .error(errorCode.getHttpStatus().name())//httpStatus 이름
                                .code(errorCode.name())//errorCode 의 이름
                                .detail(errorCode.getDetail())//errorCode 상세
                                .build()
                );
    }

    public static ResponseEntity<ErrorResponse> toResponseEntity(HttpStatus httpStatus) {
        return ResponseEntity
                .status(httpStatus)
                .body(
                        ErrorResponse.builder()
                                .status(httpStatus.value())//httpStatus 코드
                                .error(httpStatus.name())//httpStatus 이름
                                .build()
                );
    }

    public static ResponseEntity<ErrorResponse> toResponseEntity(HttpStatus httpStatus, FieldError fieldError) {
        return ResponseEntity
                .status(httpStatus)
                .body(
                        ErrorResponse.builder()
                                .status(httpStatus.value())//httpStatus 코드
                                .error(httpStatus.name())//httpStatus 이름
                                .code(fieldError.getCode())//errorCode 의 이름
                                .detail(fieldError.getDefaultMessage())//errorCode 상세
                                .build()
                );
    }
}