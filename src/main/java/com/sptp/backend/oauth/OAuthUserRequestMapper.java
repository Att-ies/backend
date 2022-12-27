package com.sptp.backend.oauth;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class OAuthUserRequestMapper {

    public OAuth2UserDto toDto(OAuth2User oAuth2User) {
        var attributes = oAuth2User.getAttributes();
        return OAuth2UserDto.builder()
                .email((String)attributes.get("email"))
                .name((String)attributes.get("name"))
                .build();
    }
}
