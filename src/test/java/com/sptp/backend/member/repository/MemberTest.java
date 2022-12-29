package com.sptp.backend.member.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    @Test
    void resetPassword() {
        //given
        Member member = Member.builder()
                .username("test")
                .userId(null)
                .email(null)
                .password("1234")
                .address(null)
                .telephone(null)
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
        String password = member.getPassword();

        //when
        member.resetPassword();
        String newPassword = member.getPassword();

        //then
        Assertions.assertThat(password).isNotEqualTo(newPassword);
        Assertions.assertThat(newPassword.length()).isEqualTo(8);
    }
}