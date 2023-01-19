package com.sptp.backend.member.web.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreferredArtistResponse {

    private Long id;
    private String nickname;
    private String education;
    private String image;
}
