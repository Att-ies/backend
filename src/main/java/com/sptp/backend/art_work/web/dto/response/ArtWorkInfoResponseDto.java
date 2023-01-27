package com.sptp.backend.art_work.web.dto.response;

import com.sptp.backend.art_work.repository.ArtWork;
import com.sptp.backend.art_work.repository.ArtWorkSize;
import com.sptp.backend.art_work_image.repository.ArtWorkImage;
import com.sptp.backend.art_work_keyword.repository.ArtWorkKeyword;
import com.sptp.backend.common.KeywordMap;
import com.sptp.backend.member.repository.Member;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtWorkInfoResponseDto {

    private ArtistDto artist;
    private ArtWorkDto artWork;
    private boolean isPreferred;

    @Data
    @Builder
    public static class ArtistDto {

        private Long id;
        private String artistEducation;
        private String artistName;
        private String artistImage;

        public static ArtistDto from(Member member, String storageUrl) {
            if (member.getImage() != null) {
                return ArtistDto.builder()
                        .id(member.getId())
                        .artistName(member.getNickname())
                        .artistEducation(member.getEducation())
                        .artistImage(storageUrl + member.getImage())
                        .build();
            }
            return ArtistDto.builder()
                    .id(member.getId())
                    .artistName(member.getNickname())
                    .artistEducation(member.getEducation())
                    .build();
        }

    }

    @Data
    @Builder
    public static class ArtWorkDto {

        private Long id;
        private String title;
        private Integer productionYear;
        private String material;
        private String genre;
        private boolean frame;
        private String description;
        private ArtWorkSize artWorkSize;
        private String guaranteeImage;
        private String mainImage;
        private List<String> keywords;
        private List<String> images;

        public static ArtWorkDto from(ArtWork artWork, List<ArtWorkImage> artWorkImages, List<ArtWorkKeyword> artWorkKeywords, String storageUrl) {

            return ArtWorkDto.builder()
                    .id(artWork.getId())
                    .title(artWork.getTitle())
                    .productionYear(artWork.getProductionYear())
                    .material(artWork.getMaterial())
                    .genre(artWork.getGenre())
                    .frame(artWork.isFrame())
                    .artWorkSize(artWork.getArtWorkSize())
                    .guaranteeImage(storageUrl + artWork.getGuaranteeImage())
                    .mainImage(storageUrl + artWork.getMainImage())
                    .description(artWork.getDescription())
                    .images(artWorkImages.stream().map(m -> storageUrl + m.getImage()).collect(Collectors.toList()))
                    .keywords(artWorkKeywords.stream().map(m -> KeywordMap.getKeywordName(m.getKeywordId())).collect(Collectors.toList()))
                    .build();
        }
    }

}
