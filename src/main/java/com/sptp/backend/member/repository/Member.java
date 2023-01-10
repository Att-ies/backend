package com.sptp.backend.member.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.sptp.backend.member.web.dto.request.ArtistUpdateRequest;
import com.sptp.backend.member.web.dto.request.MemberUpdateRequest;
import org.hibernate.annotations.BatchSize;
import org.springframework.web.multipart.MultipartFile;

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
    @BatchSize(size = 3)
    private List<String> roles = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private List<Long> preferred_artists = new ArrayList<>();

    public void changePassword(String password) {

        this.password = password;
    }

    public void pickArtist(Long artistId) {

        if(!this.preferred_artists.contains(artistId)) {
            this.preferred_artists.add(artistId);
        }
    }

    public void updateUser(MemberUpdateRequest dto, String image) {

        if (Objects.nonNull(dto.getEmail())) {
            this.email = dto.getEmail();
        }
        if (Objects.nonNull(dto.getNickname())) {
            this.nickname = dto.getNickname();
        }
        this.image=image;
    }

    public void updateArtist(ArtistUpdateRequest dto, String image) {

        if(Objects.nonNull(dto.getEmail())) {
            this.email = dto.getEmail();
        }
        if(Objects.nonNull(dto.getNickname())) {
            this.nickname = dto.getNickname();
        }
        if(Objects.nonNull(dto.getEducation())) {
            this.education = dto.getEducation();
        }
        if(Objects.nonNull(dto.getHistory())) {
            this.history = dto.getHistory();
        }
        if(Objects.nonNull(dto.getDescription())) {
            this.description = dto.getDescription();
        }
        if(Objects.nonNull(dto.getInstagram())) {
            this.instagram = dto.getInstagram();
        }
        if(Objects.nonNull(dto.getBehance())) {
            this.behance = dto.getBehance();
        }
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
