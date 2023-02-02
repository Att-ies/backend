package com.sptp.backend.exhibition.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sptp.backend.auction.repository.Auction;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExhibitionAuctionResponseDto {

    private Long id;
    private Integer turn;
    @JsonFormat(pattern = "yyyy.MM.dd")
    private LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy.MM.dd")
    private LocalDateTime endDate;
    private Integer artWorkCount;
    private String image;
    private String status;

    public static ExhibitionAuctionResponseDto from(Auction auction, String image, Integer artWorkCount) {
        return ExhibitionAuctionResponseDto.builder()
                .id(auction.getId())
                .turn(auction.getTurn())
                .startDate(auction.getStartDate())
                .endDate(auction.getEndDate())
                .image(image)
                .artWorkCount(artWorkCount)
                .status(auction.getStatus())
                .build();
    }
}
