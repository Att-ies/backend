package com.sptp.backend.member.repository;

import com.sptp.backend.keyword.repository.Keyword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String username;
    private String userId;
    private String email;
    private String password;
    private String address;
    private String telephone;
    @Builder.Default
    private String roles = "ROLE_USER";

    public void resetPassword() {

        final int PASSWORD_LENGTH = 8;
        this.password = UUID.randomUUID().toString().substring(0, PASSWORD_LENGTH);
    }
}
