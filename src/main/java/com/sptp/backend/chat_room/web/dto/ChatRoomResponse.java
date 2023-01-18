package com.sptp.backend.chat_room.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sptp.backend.member.repository.Member;
import com.sptp.backend.message.repository.Message;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ChatRoomResponse {

    private Long chatRoomId;
    private String artWorkImage;
    private int unreadCount;
    private MemberDto otherMember;
    private MessageDto lastMessage;

    @Data
    @Builder
    public static class MemberDto {
        private Long id;
        private String name;
        private String image;

        public static MemberDto of(Member member, String image) {
            return MemberDto.builder()
                    .id(member.getId())
                    .name(member.getNickname())
                    .image(image)
                    .build();
        }
    }

    @Data
    @Builder
    public static class MessageDto {
        private String message;

        @JsonFormat(pattern = "yyyy-MM-dd-HH-mm-ss")
        private LocalDateTime sendTime;

        public static MessageDto from(Message message) {
            return MessageDto.builder()
                    .message(message.getMessage())
                    .sendTime(message.getCreatedDate())
                    .build();
        }
    }
}
