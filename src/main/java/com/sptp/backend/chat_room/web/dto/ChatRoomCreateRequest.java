package com.sptp.backend.chat_room.web.dto;

import com.sptp.backend.art_work.repository.ArtWork;
import com.sptp.backend.member.repository.Member;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomCreateRequest {

    @NotNull(message = "작가 고유 번호는 필수입니다.")
    private Long artistId;

    @NotNull(message = "작품 고유 번호는 필수입니다.")
    private Long artWorkId;
}
