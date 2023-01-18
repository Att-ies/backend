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

//   경매 명과 입찰 현황, 등록 현황은 추후 경매 기능 이후에 적용
//   private String auctionName;
//   private String biddingStatus;
//   private String status;
}
