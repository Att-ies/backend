package com.sptp.backend.art_work.web.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtWorkMyListResponseDto {

    private Long id;
    private String title;
    private String artistName;

    private String auctionStatus;
    private Long biddingStatus;
    private Integer turn;
    private String image;
}
