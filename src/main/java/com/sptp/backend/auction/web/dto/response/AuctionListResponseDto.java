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

    private Long id;

    private Integer turn;

    @JsonFormat(pattern = "yyyy-MM-dd-HH-mm-ss")
    private LocalDateTime startDate;

    @JsonFormat(pattern = "yyyy-MM-dd-HH-mm-ss")
    private LocalDateTime endDate;

    private String status;
}
