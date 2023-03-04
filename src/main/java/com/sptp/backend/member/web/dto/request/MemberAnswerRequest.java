package com.sptp.backend.member.web.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberAnswerRequest {

    @ApiModelProperty(required = true, value = "문의 글 답변", example = "답변")
    private String answer;
}
