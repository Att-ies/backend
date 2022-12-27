package com.sptp.backend.member.web.dto.response;

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
    private String telephone;

}
