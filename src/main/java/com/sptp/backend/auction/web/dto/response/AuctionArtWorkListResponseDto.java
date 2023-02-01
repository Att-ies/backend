package com.sptp.backend.auction.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sptp.backend.art_work.repository.ArtWork;
import com.sptp.backend.art_work.repository.ArtWorkSize;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class AuctionArtWorkListResponseDto {

    private Integer turn;

    @JsonFormat(pattern = "yyyy-MM-dd-HH-mm-ss")
    private LocalDateTime endDate;

    private List<ArtWorkDto> artWorkList;

    @Data
    @Builder
    public static class ArtWorkDto {
        private Long id;
        private String mainImage;
        private String title;
        private ArtWorkSize artWorkSize;
        private Integer productionYear;
        private Long topPrice;
        private String material;

        public static ArtWorkDto from(ArtWork artWork, Long topPrice, String storageUrl) {
            return ArtWorkDto.builder()
                    .id(artWork.getId())
                    .title(artWork.getTitle())
                    .mainImage(storageUrl + artWork.getMainImage())
                    .artWorkSize(artWork.getArtWorkSize())
                    .productionYear(artWork.getProductionYear())
                    .topPrice(topPrice)
                    .material(artWork.getMaterial())
                    .build();
        }
    }
}
