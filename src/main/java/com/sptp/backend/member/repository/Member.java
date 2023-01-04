package com.sptp.backend.member.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.sptp.backend.member.web.dto.request.ArtistUpdateRequest;
import com.sptp.backend.member.web.dto.request.MemberUpdateRequest;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    public void changePassword(String password) {

        this.password = password;
    }

    public void updateUser(MemberUpdateRequest dto) {

        if (StringUtils.isNotBlank(dto.getEmail())) {
            this.email = dto.getEmail();
        }
        if (StringUtils.isNotBlank(dto.getUsername())) {
            this.username = dto.getUsername();
        }
        if (StringUtils.isNotBlank(dto.getImage())) {
            this.image = dto.getImage();
        }
    }

    public void updateArtist(ArtistUpdateRequest dto) {

        if(StringUtils.isNotBlank(dto.getEmail())) {
            this.email = dto.getEmail();
        }
        if(StringUtils.isNotBlank(dto.getUsername())) {
            this.username = dto.getUsername();
        }
        if(StringUtils.isNotBlank(dto.getImage())) {
            this.image = dto.getImage();
        }
        if(StringUtils.isNotBlank(dto.getEducation())) {
            this.education = dto.getEducation();
        }
        if(StringUtils.isNotBlank(dto.getHistory())) {
            this.history = dto.getHistory();
        }
        if(StringUtils.isNotBlank(dto.getDescription())) {
            this.description = dto.getDescription();
        }
        if(StringUtils.isNotBlank(dto.getInstagram())) {
            this.instagram = dto.getInstagram();
        }
        if(StringUtils.isNotBlank(dto.getBehance())) {
            this.behance = dto.getBehance();
        }
    }
}
