package com.sptp.backend.member.web.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberSaveResponseDto {

    private String nickname;
    private String userId;
    private String email;
    private String telephone;

}
