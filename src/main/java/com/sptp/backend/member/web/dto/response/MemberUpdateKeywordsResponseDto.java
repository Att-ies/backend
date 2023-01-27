package com.sptp.backend.member.web.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberUpdateKeywordsResponseDto {

    private Long id;
    private String keyword;
}
