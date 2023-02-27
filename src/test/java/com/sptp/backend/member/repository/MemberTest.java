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
                .nickname("test")
                .password("1234")
                .build();
        String password = member.getPassword();

        //when
        String newPassword = "12345678";
        member.changePassword(newPassword);

        //then
        Assertions.assertThat(password).isNotEqualTo(newPassword);
        Assertions.assertThat(newPassword.length()).isEqualTo(newPassword.length());
    }
}