package com.sptp.backend.member.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.sptp.backend.member.web.dto.request.ArtistUpdateRequest;
import com.sptp.backend.member.web.dto.request.MemberUpdateRequest;

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
    private String nickname;
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
        if (StringUtils.isNotBlank(dto.getNickname())) {
            this.nickname = dto.getNickname();
        }
        if (StringUtils.isNotBlank(dto.getImage())) {
            this.image = dto.getImage();
        }
    }

    public void updateArtist(ArtistUpdateRequest dto) {

        if(StringUtils.isNotBlank(dto.getEmail())) {
            this.email = dto.getEmail();
        }
        if(StringUtils.isNotBlank(dto.getNickname())) {
            this.nickname = dto.getNickname();
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

    // 이메일이 수정됐는지 확인. 본인 이메일 그대로거나, 비어있을 경우 수정되지 않은 걸로 간주해 false 반환.
    public boolean isUpdatedEmail(String email){
        if(StringUtils.isNotBlank(email) && !email.equals(this.email)){
            return true;
        }
        return false;
    }

    public boolean isUpdatedNickname(String nickname){
        if(StringUtils.isNotBlank(nickname) && !nickname.equals(this.nickname)){
            return true;
        }
        return false;
    }
}
