package com.sptp.backend.domain.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String username;
    private String email;
    private String address;
    private String tel;

    public Member(String username, String email, String address, String tel) {
        this.username = username;
        this.email = email;
        this.address = address;
        this.tel = tel;
    }
}
