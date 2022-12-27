package com.sptp.backend.controller.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberSaveResponseDto {

    private String username;
    private String userId;
    private String email;
    private String address;
    private String tel;

}
