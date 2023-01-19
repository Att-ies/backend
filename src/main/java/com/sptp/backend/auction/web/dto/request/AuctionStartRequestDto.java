package com.sptp.backend.auction.web.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuctionStartRequestDto {

    private Integer turn;
}
