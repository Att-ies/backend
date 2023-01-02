package com.sptp.backend.author.repository;

import com.sptp.backend.member.repository.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"userId", "email"})})
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String username;
    private String userId;
    private String email;
    private String password;
    private String address;
    private String telephone;
    private String image;
    @Builder.Default
    private String roles = "ROLE_AUTHOR";
    private String education;
    private String history;
    private String description;
    private String instagram;
    private String behance;

    public void resetPassword() {

        final int PASSWORD_LENGTH = 8;
        this.password = UUID.randomUUID().toString().substring(0, PASSWORD_LENGTH);
    }
}

