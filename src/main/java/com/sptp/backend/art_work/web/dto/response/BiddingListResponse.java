package com.sptp.backend.art_work.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sptp.backend.art_work.repository.ArtWork;
import com.sptp.backend.auction.repository.Auction;
import com.sptp.backend.bidding.repository.Bidding;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class BiddingListResponse {

    private ArtWorkDto artWork;
    private AuctionDto auction;
    private List<BiddingDto> biddingList;
    private Integer totalBiddingCount;

    @Data
    @Builder
    public static class ArtWorkDto {
        private Long id;
        private String title;
        private String artistName;
        private String genre;
        private Long beginPrice;
        private Long topPrice;

        public static ArtWorkDto of(ArtWork artWork, Long topPrice) {
            return ArtWorkDto.builder()
                    .id(artWork.getId())
                    .title(artWork.getTitle())
                    .artistName(artWork.getMember().getNickname())
                    .genre(artWork.getGenre())
                    .beginPrice(Long.valueOf(artWork.getPrice()))
                    .topPrice(topPrice)
                    .build();
        }
    }

    @Data
    @Builder
    public static class AuctionDto {
        private Long id;

        @JsonFormat(pattern = "yyyy-MM-dd-HH-mm-ss")
        private LocalDateTime startDate;

        @JsonFormat(pattern = "yyyy-MM-dd-HH-mm-ss")
        private LocalDateTime endDate;

        public static AuctionDto from(Auction auction) {
            return AuctionDto.builder()
                    .id(auction.getId())
                    .startDate(auction.getStartDate())
                    .endDate(auction.getEndDate())
                    .build();
        }
    }

    @Data
    @Builder
    public static class BiddingDto {
        private Long id;
        private String memberName;
        private Long price;

        @JsonFormat(pattern = "yyyy-MM-dd-HH-mm-ss")
        private LocalDateTime date;

        public static BiddingDto from(Bidding bidding) {
            return BiddingDto.builder()
                    .id(bidding.getId())
                    .memberName(bidding.getMember().getNickname())
                    .price(bidding.getPrice())
                    .date(bidding.getModifiedDate())
                    .build();
        }
    }
}
