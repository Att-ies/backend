package com.sptp.backend.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationCode {

    MEMBER_TO_ARTIST("작가 등록 완료", "아띠즈 공식 작가로 등록되었어요");

    private final String title;
    private final String message;
}
