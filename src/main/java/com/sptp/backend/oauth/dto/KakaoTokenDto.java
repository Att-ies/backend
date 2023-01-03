package com.sptp.backend.oauth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KakaoTokenDto {

    private String accessToken;
    private String refreshToken;
    private String idToken;
}
