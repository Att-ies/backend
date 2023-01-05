package com.sptp.backend.member.web.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorSaveRequestDto {

    private String nickname;
    private String userId;
    private String email;
    private String password;
    private String telephone;

    private String education;
    private String history;
    private String description;
    private String instagram;
    private String behance;
}
