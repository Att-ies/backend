package com.sptp.backend.art_work.web.dto.response;

import com.sptp.backend.art_work.repository.ArtWork;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtWorkTerminatedListResponseDto {

    private boolean nextPage;
    private List<ArtWorkDto> artWorks;

    @Data
    @Builder
    public static class ArtWorkDto {
        private Long id;
        private String mainImage;
        private String title;
        private String material;
        private Long topPrice;
        private Integer biddingCount;
        private String status;

        public static ArtWorkDto from(ArtWork artWork, Long topPrice, Integer biddingCount) {
            return ArtWorkDto.builder()
                    .id(artWork.getId())
                    .mainImage(artWork.getMainImage())
                    .title(artWork.getTitle())
                    .material(artWork.getMaterial())
                    .topPrice(topPrice)
                    .biddingCount(biddingCount)
                    .status(artWork.getSaleStatus())
                    .build();
        }
    }
}
