package com.sptp.backend.art_work.web.dto.response;

import com.sptp.backend.art_work.repository.ArtWork;
import com.sptp.backend.member.repository.Member;
import lombok.*;

@Data
@Builder
public class ArtWorkDeliveryResponse {

    private ArtWorkDto artWork;
    private MemberDto artist;
    private MemberDto member;

    @Data
    @Builder
    public static class ArtWorkDto {
        private Long id;
        private String title;
        private Long topPrice;

        public static ArtWorkDto from (ArtWork artWork, Long topPrice) {
            return ArtWorkDto.builder()
                    .id(artWork.getId())
                    .title(artWork.getTitle())
                    .topPrice(topPrice)
                    .build();
        }
    }

    @Data
    @Builder
    public static class MemberDto {
        private Long id;
        private String address;
        private String telephone;

        public static MemberDto from (Member member) {
            return MemberDto.builder()
                    .id(member.getId())
                    .address(member.getAddress())
                    .telephone(member.getTelephone())
                    .build();
        }
    }
}
