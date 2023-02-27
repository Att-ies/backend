package com.sptp.backend.member.web.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenResponseDto {

    private String accessToken;
    private String refreshToken;
    private String roles;
}
