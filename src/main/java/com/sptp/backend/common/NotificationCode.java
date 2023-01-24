package com.sptp.backend.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationCode {

    MEMBER_TO_ARTIST("작가 등록 완료",
            "아띠즈 공식 작가로 등록되었어요",
            "작가 프로필 완성하러 가기"),
    SAVE_ARTWORK("작품 등록 완료",
            " 작품 등록이 완료되었습니다.",
            "곧 경매와 전시회에 작품이 올라가니 조금만 기다려주세요."),
    SAVE_AUCTION("경매 등록 알림",
            " 작품이 경매에 등록되었습니다.",
            "클릭 시, 경매 상세페이지로 이동"),
    SAVE_DISPLAY("전시회 등록 알림",
            " 작품이 전시회에 등록되었습니다.",
            "클릭 시, 전시회 페이지로 이동"),
    MEMBER_ASK_RESPONSE("1대1 문의 알림",
            "1대1 문의 답변이 완료되었어요.",
            "클릭 시, 1대1 문의 목록 페이지로 이동"),
    SUGGEST_BID("응찰 알림",
            "원에 응찰하였습니다.",
            "클릭 시, 응찰 페이지로 이동"),
    SUCCESSFUL_BID("입찰 알림",
            "원에 입찰하였습니다.",
            "클릭 시, 응찰 페이지로 이동"),
    FAILED_BID("입찰 알림",
            "을 누군가 입찰했어요. 바로 확인해보세요.",
            "클릭 시, 응찰 페이지로 이동"),
    SUCCESSFUL_AUCTION("작품 낙찰 성공",
            " 작품이 낙찰되었습니다.",
            "클릭 시, 구입 목록 페이지로 이동"),
    FAILED_AUCTION("작품 유찰 알림",
            " 작품이 유찰되었어요.",
            null),
    CHATTING("채팅",
            "건의 읽지 않은 채팅이 있습니다.",
            "클릭 시, 채팅 목록 페이지 이동");

    private final String title;
    private final String message;
    private final String details;
}


