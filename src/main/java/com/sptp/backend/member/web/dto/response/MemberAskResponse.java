package com.sptp.backend.member.web.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberAskResponse {

    private Long id;
    private String title;
    private String content;
    private String answer;
    private String status;
}
