package com.sptp.backend.member.web.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberUpdateRequest {

    private String nickname;
    private String email;
    private String address;
    private String[] keywords;
}