package com.sptp.backend.controller.member.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberSaveRequestDto {

    private String username;
    private String userId;
    private String email;
    private String password;
    private String address;
    private String tel;

}
