package com.sptp.backend.auction.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuctionListResponseDto {

    Integer turn;

    @JsonFormat(pattern = "yyyy-MM-dd-HH-mm-ss")
    LocalDateTime startDate;

    @JsonFormat(pattern = "yyyy-MM-dd-HH-mm-ss")
    LocalDateTime endDate;

    String status;
}
