package com.sptp.backend.member.web.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberLoginRequestDto {

    @ApiModelProperty(required = true, value = "유저 아이디", example = "test1234")
    private String userId;
    @ApiModelProperty(required = true, value = "유저 패스워드", example = "password1234")
    private String password;
}
