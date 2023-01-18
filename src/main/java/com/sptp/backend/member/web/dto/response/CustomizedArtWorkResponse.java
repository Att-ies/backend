package com.sptp.backend.member.web.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomizedArtWorkResponse {

    private String title;
    private String education;
    private String image;
}
