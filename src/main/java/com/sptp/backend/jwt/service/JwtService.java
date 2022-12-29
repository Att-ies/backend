package com.sptp.backend.jwt.service;

import com.sptp.backend.common.exception.CustomException;
import com.sptp.backend.common.exception.ErrorCode;
import com.sptp.backend.jwt.web.JwtTokenProvider;
import com.sptp.backend.jwt.web.dto.TokenDto;
import com.sptp.backend.jwt.repository.RefreshToken;
import com.sptp.backend.jwt.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void saveRefreshToken(TokenDto tokenDto){

        RefreshToken refreshToken = RefreshToken.builder().keyUserId(tokenDto.getKey()).refreshToken(tokenDto.getRefreshToken()).build();
        String userId = refreshToken.getKeyUserId();
        if(refreshTokenRepository.existsByKeyUserId(userId)){
            log.info("기존의 존재하는 refresh 토큰 삭제");
            refreshTokenRepository.deleteByKeyUserId(userId);
        }
        refreshTokenRepository.save(refreshToken);

    }

    public RefreshToken getRefreshToken(String refreshToken){

        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new CustomException(ErrorCode.TOKEN_INVALID));
    }

    public String validateRefreshToken(String refreshToken){

        //DB에서 Refresh Token 조회
        RefreshToken getRefreshToken = getRefreshToken(refreshToken);

        String createdAccessToken = jwtTokenProvider.validateRefreshToken(getRefreshToken);

        if (createdAccessToken == null) {
            throw new CustomException(ErrorCode.TOKEN_EXPIRED);
        }

        return createdAccessToken;
    }

}