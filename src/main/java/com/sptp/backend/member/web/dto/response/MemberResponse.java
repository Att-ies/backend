package com.sptp.backend.member.web.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberResponse {

    private String nickname;
    private String userId;
    private String email;
    private String telephone;
    private String image;

    private String education;
    private String history;
    private String description;
    private String instagram;
    private String behance;
}
