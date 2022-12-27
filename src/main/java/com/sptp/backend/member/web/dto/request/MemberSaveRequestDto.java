package com.sptp.backend.member.web.dto.request;

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
    private String telephone;

}
