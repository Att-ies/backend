package com.sptp.backend.controller.member.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberSaveResponseDto {

    private String username;
    private String userId;
    private String email;
    private String address;
    private String tel;

}
