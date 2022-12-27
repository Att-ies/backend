package com.sptp.backend.oauth;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class OAuth2UserDto {

    private String email;
    private String name;

    @Builder
    public OAuth2UserDto(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
