package com.sptp.backend.member.web.dto.response;

import com.sptp.backend.art_work.repository.ArtWork;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomizedArtWorkResponse {

    private boolean nextPage;
    private List<CustomizedArtWorkResponse.ArtWorkDto> artworks;

    @Data
    @Builder
    public static class ArtWorkDto {
        private Long id;
        private String title;
        private String education;
        private String image;

        public static ArtWorkDto from (ArtWork artWork, String awsStorageUrl) {
            return ArtWorkDto.builder()
                    .id(artWork.getId())
                    .title(artWork.getTitle())
                    .education(artWork.getMember().getEducation())
                    .image(awsStorageUrl + artWork.getMainImage())
                    .build();
        }
    }
}
