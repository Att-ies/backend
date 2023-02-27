package com.sptp.backend.oauth.controller;

import com.sptp.backend.jwt.web.dto.TokenDto;
import com.sptp.backend.member.web.dto.response.TokenResponseDto;
import com.sptp.backend.oauth.dto.NaverTokenDto;
import com.sptp.backend.oauth.service.NaverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class OAuth2NaverController {

    private final NaverService naverService;

    @GetMapping("/oauth2/naver")
    public ResponseEntity<?> naverCallback(@RequestParam("code") String code, @RequestParam("state") String state) throws IOException {

        NaverTokenDto naverTokenDto = naverService.getNaverToken(code, state);
        TokenDto tokenDto = naverService.loginWithNaver(naverTokenDto);

        TokenResponseDto tokenResponseDto = TokenResponseDto.builder()
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
                .roles(tokenDto.getGrantType())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(tokenResponseDto);
    }
}
