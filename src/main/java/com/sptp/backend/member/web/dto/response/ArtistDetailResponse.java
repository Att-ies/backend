package com.sptp.backend.member.web.dto.response;

import com.sptp.backend.art_work.repository.ArtWork;
import com.sptp.backend.art_work.repository.ArtWorkStatus;
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

        private Long id;
        private String nickname;
        private String image;
        private String education;
        private String history;
        private String description;
        private String instagram;
        private String behance;

        public static MemberDto from(Member member, String awsStorageUrl) {

            String image = awsStorageUrl + member.getImage();
            if(member.getImage() == null) {
                 image = null;
            }

            System.out.println("작가dto");
            return MemberDto.builder()
                    .id(member.getId())
                    .nickname(member.getNickname())
                    .image(image)
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

        private Long id;
        private String title;
        private String image;

        public static ArtWorkDto from(ArtWork artwork, String awsStorageUrl) {

            System.out.println("작품dto");
            return ArtWorkDto.builder()
                    .id(artwork.getId())
                    .title(artwork.getTitle())
                    .image(awsStorageUrl + artwork.getMainImage())
                    .build();
        }
    }
}
