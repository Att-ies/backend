package com.sptp.backend.member.web.dto.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MemberResponse {

    private String nickname;
    private String userId;
    private String email;
    private String telephone;
    private String image;
    private List<String> keywords;
}
