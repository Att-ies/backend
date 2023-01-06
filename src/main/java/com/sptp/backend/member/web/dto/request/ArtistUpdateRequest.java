package com.sptp.backend.member.web.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistUpdateRequest {

    private String nickname;
    private String email;
    private String education;
    private String history;
    private String description;
    private String instagram;
    private String behance;
}
