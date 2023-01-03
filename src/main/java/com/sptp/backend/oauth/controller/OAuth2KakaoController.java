package com.sptp.backend.oauth.controller;

import com.sptp.backend.jwt.web.dto.TokenDto;
import com.sptp.backend.member.web.dto.response.TokenResponseDto;
import com.sptp.backend.oauth.dto.KakaoTokenDto;
import com.sptp.backend.oauth.service.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class OAuth2KakaoController {

    private final KakaoService kakaoService;

    @GetMapping("/oauth2/kakao")
    public ResponseEntity<?> kakaoCallback(@RequestParam("code") String code) throws IOException {

        KakaoTokenDto kakaoTokenDto = kakaoService.getKakaoToken(code);
        TokenDto tokenDto = kakaoService.loginWithKakao(kakaoTokenDto);

        TokenResponseDto tokenResponseDto = TokenResponseDto.builder()
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(tokenResponseDto);
    }
}
