package com.sptp.backend.member.service;

import com.sptp.backend.common.exception.CustomException;
import com.sptp.backend.common.exception.ErrorCode;
import com.sptp.backend.jwt.repository.RefreshTokenRepository;
import com.sptp.backend.jwt.service.JwtService;
import com.sptp.backend.jwt.web.JwtTokenProvider;
import com.sptp.backend.MockPasswordEncoder;
import com.sptp.backend.member.repository.Member;
import com.sptp.backend.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    MemberService memberService;

    @Mock
    MemberRepository memberRepository;

    @Spy
    PasswordEncoder passwordEncoder = new MockPasswordEncoder();

    @Nested
    class changePasswordTest {
        long id = 1L;
        String password = "12345678";
        Member member;

        @BeforeEach
        void init() {
            member = Member.builder()
                    .id(id)
                    .userId("chlwnsdud")
                    .password(password)
                    .build();
        }

        @Test
        void success() {
            //given
            when(memberRepository.findById(anyLong()))
                    .thenReturn(Optional.of(member));

            //when
            String newPassword = "newPassword";
            memberService.changePassword(id, newPassword);

            //then
            Assertions.assertThat(member.getPassword()).isEqualTo(passwordEncoder.encode(newPassword));
        }

        @Test
        void failByNotFoundMember() {
            //given
            when(memberRepository.findById(anyLong()))
                    .thenReturn(Optional.empty());

            //when
            //then
            Assertions.assertThatThrownBy(() -> memberService.changePassword(id, "newPassword"))
                    .isInstanceOf(CustomException.class);
        }

        @Test
        void failBySamePassword() {
            //given
            when(memberRepository.findById(anyLong()))
                    .thenReturn(Optional.empty());

            //when
            //then
            Assertions.assertThatThrownBy(() -> memberService.changePassword(id, password))
                    .isInstanceOf(CustomException.class);
        }
    }
}