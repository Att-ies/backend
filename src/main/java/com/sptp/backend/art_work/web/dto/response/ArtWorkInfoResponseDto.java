package com.sptp.backend.art_work.web.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @Data
    @Builder
    public static class ArtistDto {
        private String artistEducation;
        private String artistName;
        private String artistImage;

        public static ArtistDto from(Member member) {
            return ArtistDto.builder()
                    .artistName(member.getNickname())
                    .artistEducation(member.getEducation())
                    .artistImage(member.getImage())
                    .build();
        }

    }

    @Data
    @Builder
    public static class ArtWorkDto {

        private String title;
        private Integer productionYear;
        private String material;
        private String genre;
        private boolean frame;
//        private Integer width;
//        private Integer length;
//        private Integer height;
//        private Integer size;
        private String description;
        private ArtWorkSize artWorkSize;
        private String guaranteeImage;
        private String mainImage;
        private List<String> keywords;
        private List<String> images;

        public static ArtWorkDto from(ArtWork artWork, List<ArtWorkImage> artWorkImages, List<ArtWorkKeyword> artWorkKeywords) {
            return ArtWorkDto.builder()
                    .title(artWork.getTitle())
                    .productionYear(artWork.getProductionYear())
                    .material(artWork.getMaterial())
                    .genre(artWork.getGenre())
                    .frame(artWork.isFrame())
                    .artWorkSize(artWork.getArtWorkSize())
                    .guaranteeImage(artWork.getGuaranteeImage())
                    .mainImage(artWork.getMainImage())
                    .images(artWorkImages.stream().map(m -> m.getImage()).collect(Collectors.toList()))
                    .keywords(artWorkKeywords.stream().map(m-> KeywordMap.getKeywordName(m.getKeywordId())).collect(Collectors.toList()))
                    .build();
        }
    }

}
