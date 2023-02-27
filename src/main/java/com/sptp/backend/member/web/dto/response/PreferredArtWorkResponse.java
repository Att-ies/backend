package com.sptp.backend.member.web.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreferredArtWorkResponse {

    private Long id;
    private String title;
    private Long price;
    private String image;
    private String saleStatus;
    private String artist;
    private boolean hot;
    private boolean pick;
}
