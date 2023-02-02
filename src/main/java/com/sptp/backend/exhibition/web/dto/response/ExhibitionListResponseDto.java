package com.sptp.backend.exhibition.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sptp.backend.auction.repository.Auction;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExhibitionListResponseDto {

    private List<ProcessingAuctionDto> processingAuction;
    private List<TerminatedAuctionDto> terminatedAuction;

    @Data
    @Builder
    public static class ProcessingAuctionDto {

        private Long id;
        private Integer turn;
        @JsonFormat(pattern = "yyyy.MM.dd")
        private LocalDateTime startDate;
        @JsonFormat(pattern = "yyyy.MM.dd")
        private LocalDateTime endDate;
        private Integer artWorkCount;
        private String image;

        public static ProcessingAuctionDto from(Auction auction, String image ,Integer artWorkCount) {
            return ProcessingAuctionDto.builder()
                    .id(auction.getId())
                    .turn(auction.getTurn())
                    .startDate(auction.getStartDate())
                    .endDate(auction.getEndDate())
                    .image(image)
                    .artWorkCount(artWorkCount)
                    .build();
        }
    }

    @Data
    @Builder
    public static class TerminatedAuctionDto {

        private Long id;
        private Integer turn;
        @JsonFormat(pattern = "yyyy.MM.dd")
        private LocalDateTime startDate;
        @JsonFormat(pattern = "yyyy.MM.dd")
        private LocalDateTime endDate;
        private Integer artWorkCount;
        private String image;

        public static TerminatedAuctionDto from(Auction auction, String image, Integer artWorkCount) {
            return TerminatedAuctionDto.builder()
                    .id(auction.getId())
                    .turn(auction.getTurn())
                    .startDate(auction.getStartDate())
                    .endDate(auction.getEndDate())
                    .image(image)
                    .artWorkCount(artWorkCount)
                    .build();
        }
    }

}
