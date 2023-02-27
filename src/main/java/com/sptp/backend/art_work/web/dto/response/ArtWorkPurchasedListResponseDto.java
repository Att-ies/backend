package com.sptp.backend.art_work.web.dto.response;

import com.sptp.backend.art_work.repository.ArtWork;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtWorkPurchasedListResponseDto {

    private List<BiddingDto> biddingList;
    private List<SuccessfulBiddingDto> successfulBiddingList;

    @Data
    @Builder
    public static class BiddingDto {

        private Long id;
        private String mainImage;
        private Integer turn;
        private String title;
        private String artistName;
        private Long myBiddingPrice;
        private Long finalBiddingPrice;

        public static BiddingDto from(ArtWork artWork, Long myBiddingPrice, Long finalBiddingPrice, String storageUrl) {
            return BiddingDto.builder()
                    .id(artWork.getId())
                    .mainImage(storageUrl + artWork.getMainImage())
                    .turn(artWork.getAuction().getTurn())
                    .title(artWork.getTitle())
                    .artistName(artWork.getMember().getNickname())
                    .myBiddingPrice(myBiddingPrice)
                    .finalBiddingPrice(finalBiddingPrice)
                    .build();
        }
    }

    @Data
    @Builder
    public static class SuccessfulBiddingDto {

        private Long id;
        private String mainImage;
        private Integer turn;
        private String title;
        private String artistName;
        private Long finalBiddingPrice;

        public static SuccessfulBiddingDto from(ArtWork artWork, Long finalBiddingPrice, String storageUrl) {
            return SuccessfulBiddingDto.builder()
                    .id(artWork.getId())
                    .mainImage(storageUrl + artWork.getMainImage())
                    .turn(artWork.getAuction().getTurn())
                    .title(artWork.getTitle())
                    .artistName(artWork.getMember().getNickname())
                    .finalBiddingPrice(finalBiddingPrice)
                    .build();
        }
    }

}
