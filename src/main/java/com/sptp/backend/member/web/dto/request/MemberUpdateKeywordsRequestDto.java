package com.sptp.backend.member.web.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberUpdateKeywordsRequestDto {

    private List<String> keywords;
}
