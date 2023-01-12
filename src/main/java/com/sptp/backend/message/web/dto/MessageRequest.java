package com.sptp.backend.message.web.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageRequest {

    @NotNull(message = "채팅방 고유 번호는 필수입니다.")
    private Long chatRoomId;

    @NotBlank(message = "채팅 메세지를 입력해주세요.")
    private String message;
}
