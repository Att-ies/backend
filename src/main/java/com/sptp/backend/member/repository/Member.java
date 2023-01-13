package com.sptp.backend.member.repository;

import com.sptp.backend.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.sptp.backend.member.web.dto.request.ArtistUpdateRequest;
import com.sptp.backend.member.web.dto.request.MemberUpdateRequest;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@BatchSize(size = 50)
public class Member extends BaseEntity {

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

    public void changeToArtist() {

        this.roles = Collections.singletonList("ROLE_ARTIST");
    }

    public void updateUser(MemberUpdateRequest dto, String image) {

        this.email = dto.getEmail();
        this.nickname = dto.getNickname();
        // image 수정 여부 체크. null 값으로 들어올 수도 있어서 null로 체크하면 안됨
        if(dto.getIsChanged()) {
            this.image=image;
        }
    }

    public void updateArtist(ArtistUpdateRequest dto, String image) {

        this.email = dto.getEmail();
        this.nickname = dto.getNickname();
        this.education = dto.getEducation();
        this.history = dto.getHistory();
        this.description = dto.getDescription();
        this.instagram = dto.getInstagram();
        this.behance = dto.getBehance();
        this.image = image;
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

    public boolean isBlankImage() {
        if(StringUtils.isBlank(this.image)) {
            return true;
        }
        return false;
    }
}
