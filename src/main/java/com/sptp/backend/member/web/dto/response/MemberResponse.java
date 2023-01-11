package com.sptp.backend.member.web.dto.response;

import lombok.*;

import java.util.List;

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
    private List<String> keywords;

    private String education;
    private String history;
    private String description;
    private String instagram;
    private String behance;
}
