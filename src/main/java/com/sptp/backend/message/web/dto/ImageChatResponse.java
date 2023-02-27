package com.sptp.backend.message.web.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageChatResponse {

    private Long chatRoomId;

    private String image;
}
