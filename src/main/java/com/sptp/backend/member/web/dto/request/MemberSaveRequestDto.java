package com.sptp.backend.member.web.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberSaveRequestDto {

    private String nickname;
    private String userId;
    private String email;
    private String password;
    private String telephone;
    private List<String> keywords;

}
