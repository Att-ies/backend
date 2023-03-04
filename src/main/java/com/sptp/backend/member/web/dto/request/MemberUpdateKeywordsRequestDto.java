package com.sptp.backend.member.web.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberUpdateKeywordsRequestDto {

    @ApiModelProperty(required = true, value = "유저 관심 키워드", example = "['화려한','유화']")
    private List<String> keywords;
}
