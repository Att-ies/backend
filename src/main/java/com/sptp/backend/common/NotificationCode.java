package com.sptp.backend.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationCode {

    MEMBER_TO_ARTIST("작가 등록 완료",
            "아띠즈 공식 작가로 등록되었어요",
            "작가 프로필 완성하러 가기",
            null),
    SAVE_ARTWORK("작품 등록 완료",
            " 작품 등록이 완료되었습니다.",
            "곧 경매와 전시회에 작품이 올라가니 조금만 기다려주세요.",
            null),

    CHATTING("채팅",
            "건의 읽지 않은 채팅이 있습니다.",
            "클릭 시, 채팅 목록 페이지 이동",
            "/chat-rooms");

    private final String title;
    private final String message;
    private final String details;
    private final String link;
}


