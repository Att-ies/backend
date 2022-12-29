package com.sptp.backend.member.repository;

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

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    public Member(String username, String userId, String email, String password, String address, String telephone, List<String> roles) {
        this.username = username;
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.address = address;
        this.telephone = telephone;
        this.roles = roles;
    }

    public void resetPassword() {

        final int PASSWORD_LENGTH = 8;
        this.password = UUID.randomUUID().toString().substring(PASSWORD_LENGTH);
    }
}
