package com.sptp.backend.art_work.web.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtWorkBidRequest {

    @NotNull(message = "응찰 가격을 입력해주세요.")
    private Long price;
}
