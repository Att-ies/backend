package com.sptp.backend.chat_room.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sptp.backend.member.repository.Member;
import com.sptp.backend.message.repository.Message;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ChatRoomDetailResponse {

    private Long chatRoomId;
    private MemberDto artist;
    private MemberDto member;
    private List<MessageDto> messages;

    @Data
    @Builder
    public static class MemberDto {
        private Long id;
        private String name;
//        TODO private String responseTime; 추후 구현 예정

        public static MemberDto from(Member member) {
            return MemberDto.builder()
                    .id(member.getId())
                    .name(member.getNickname())
                    .build();
        }
    }

    @Data
    @Builder
    public static class MessageDto {
        private Long senderId;
        private String message;

        @JsonFormat(pattern = "yyyy-MM-dd-HH-mm-ss")
        private LocalDateTime sendDate;

        public static MessageDto from(Message message) {
            return MessageDto.builder()
                    .senderId(message.getSender().getId())
                    .message(message.getMessage())
                    .sendDate(message.getCreatedDate())
                    .build();
        }
    }
}
