package com.sptp.backend.member.web.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreferredArtWorkResponse {

    private String title;

    private Integer price;

    private String image;
}
