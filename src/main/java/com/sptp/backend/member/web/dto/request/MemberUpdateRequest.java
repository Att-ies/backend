package com.sptp.backend.member.web.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberUpdateRequest {

    private String nickname;
    private String email;
    private String address;
    private String telephone;
    private Boolean isChanged;
}