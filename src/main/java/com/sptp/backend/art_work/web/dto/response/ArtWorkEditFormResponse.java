package com.sptp.backend.art_work.web.dto.response;

import com.sptp.backend.art_work.repository.ArtWork;
import com.sptp.backend.art_work.repository.ArtWorkSize;
import com.sptp.backend.art_work_image.repository.ArtWorkImage;
import com.sptp.backend.art_work_keyword.repository.ArtWorkKeyword;
import com.sptp.backend.common.KeywordMap;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtWorkEditFormResponse {

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
    private Long price;
    private String status;
    private String statusDescription;

    public static ArtWorkEditFormResponse from(ArtWork artWork, List<ArtWorkImage> artWorkImages, List<ArtWorkKeyword> artWorkKeywords, String storageUrl) {

        return ArtWorkEditFormResponse.builder()
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
                .price(artWork.getPrice())
                .status(artWork.getStatus())
                .statusDescription(artWork.getStatusDescription())
                .build();
    }
}
