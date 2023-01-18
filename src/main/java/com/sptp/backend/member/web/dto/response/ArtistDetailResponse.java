package com.sptp.backend.member.web.dto.response;

import com.sptp.backend.art_work.repository.ArtWork;
import com.sptp.backend.member.repository.Member;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ArtistDetailResponse {

    private MemberDto member;
    private List<ArtWorkDto> artworks;

    @Data
    @Builder
    public static class MemberDto {
        private String nickname;
        private String image;
        private String education;
        private String history;
        private String description;
        private String instagram;
        private String behance;

        public static MemberDto from(Member member) {
            return MemberDto.builder()
                    .nickname(member.getNickname())
                    .image(member.getImage())
                    .education(member.getEducation())
                    .history(member.getHistory())
                    .description(member.getDescription())
                    .instagram(member.getInstagram())
                    .behance(member.getBehance())
                    .build();
        }
    }

    @Data
    @Builder
    public static class ArtWorkDto {
        private String title;
        private String image;
        // 경매 상태 추후 구현 예정

        public static ArtWorkDto from(ArtWork artwork) {
            return ArtWorkDto.builder()
                    .title(artwork.getTitle())
                    .image(artwork.getMainImage())
                    .build();
        }
    }


}
