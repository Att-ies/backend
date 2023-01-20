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

    private Integer price;

    private String image;
}
