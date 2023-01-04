package com.sptp.backend.member.repository;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.sptp.backend.keyword.repository.Keyword;
import com.sptp.backend.member.web.dto.request.MemberUpdateRequest;
import lombok.*;

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

    // 공통 컬럼
    private String username;
    private String userId;
    private String email;
    private String password;
    private String address;
    private String telephone;
    private String image;

    // 작가 컬럼
    private String education;
    private String history;
    private String description;
    private String instagram;
    private String behance;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    public void resetPassword() {

        final int PASSWORD_LENGTH = 8;
        this.password = UUID.randomUUID().toString().substring(0, PASSWORD_LENGTH);
    }

    public void updateUser(MemberUpdateRequest dto) {

        if (StringUtils.isNotBlank(dto.getEmail()))
            this.email = dto.getEmail();
        if (StringUtils.isNotBlank(dto.getUsername()))
            this.username = dto.getUsername();
        if (StringUtils.isNotBlank(dto.getImage()))
            this.image = dto.getImage();
    }
}
