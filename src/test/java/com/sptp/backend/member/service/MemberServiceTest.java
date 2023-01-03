package com.sptp.backend.member.service;

import com.sptp.backend.jwt.repository.RefreshTokenRepository;
import com.sptp.backend.jwt.service.JwtService;
import com.sptp.backend.jwt.web.JwtTokenProvider;
import com.sptp.backend.MockPasswordEncoder;
import com.sptp.backend.member.repository.Member;
import com.sptp.backend.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
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

    @Test
    void changePassword() {
        //given
        long id = 1L;
        Member member = Member.builder()
                .id(id)
                .userId("chlwnsdud")
                .password("12345678")
                .build();

        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(member));
        System.out.println(member.getPassword());

        //when
        String newPassword = "newPassword";
        memberService.changePassword(id, newPassword);

        //then
        Assertions.assertThat(member.getPassword()).isEqualTo(passwordEncoder.encode(newPassword));
    }
}