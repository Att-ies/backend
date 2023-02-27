package com.sptp.backend.message.web.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageChatRequest {

    @NotNull(message = "보내는 사람의 고유 번호는 필수입니다.")
    private Long senderId;

    @NotNull(message = "채팅방 고유 번호는 필수입니다.")
    private Long chatRoomId;

    @NotBlank(message = "이미지를 첨부해서 보내주세요.")
    private String encodedImage;
}
