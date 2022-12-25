package com.sptp.backend.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sptp.backend.controller.member.dto.response.TokenResponseDto;
import com.sptp.backend.domain.member.entity.Member;
import com.sptp.backend.domain.member.repository.MemberRepository;
import com.sptp.backend.domain.member.service.MemberService;
import com.sptp.backend.jwt.JwtTokenProvider;
import com.sptp.backend.jwt.dto.TokenDto;
import com.sptp.backend.jwt.service.JwtService;
import com.sptp.backend.oauth.dto.OAuth2UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final OAuthUserRequestMapper oAuthUserRequestMapper;
    private final ObjectMapper objectMapper;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        OAuth2UserDto userDto = oAuthUserRequestMapper.toDto(oAuth2User);

        System.out.println("userDto = " + userDto.getEmail());
        System.out.println("userDto.getName() = " + userDto.getName());

        String email = userDto.getEmail();
        String username = userDto.getName();

        // 처음 로그인일 경우 회원가입 로직 실행
        if (memberRepository.existsByEmail(email) == false) {
            Member member = Member.builder().email(email).username(username).roles(Collections.singletonList("ROLE_USER")).build();
            memberRepository.save(member);

            TokenDto tokenDto = jwtTokenProvider.createToken(email, member.getRoles());
            jwtService.saveRefreshToken(tokenDto);

            TokenResponseDto tokenResponseDto = new TokenResponseDto(tokenDto.getAccessToken(), tokenDto.getRefreshToken());

            writeTokenResponse(response, tokenResponseDto);
        }else{
            Optional<Member> member = memberRepository.findByEmail(email);

            TokenDto tokenDto = jwtTokenProvider.createToken(email, member.get().getRoles());
            jwtService.saveRefreshToken(tokenDto);

            TokenResponseDto tokenResponseDto = new TokenResponseDto(tokenDto.getAccessToken(), tokenDto.getRefreshToken());

            writeTokenResponse(response, tokenResponseDto);
        }


    }

    private void writeTokenResponse(HttpServletResponse response, TokenResponseDto tokenResponseDto)
            throws IOException {
        response.setContentType("text/html;charset=UTF-8");

        response.addHeader("access_token", tokenResponseDto.getAccessToken());
        response.addHeader("refresh_token", tokenResponseDto.getRefreshToken());
        response.setContentType("application/json;charset=UTF-8");

        var writer = response.getWriter();
        writer.println(objectMapper.writeValueAsString(tokenResponseDto));
        writer.flush();
    }


}
