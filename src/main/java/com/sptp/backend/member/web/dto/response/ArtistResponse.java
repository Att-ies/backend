package com.sptp.backend.member.web.dto.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ArtistResponse extends MemberResponse {

    private String education;
    private String history;
    private String description;
    private String instagram;
    private String behance;
}
