package com.sptp.backend.member.web.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberSaveRequestDto {

    @ApiModelProperty(required = true, value = "유저 닉네임", example = "아라")
    private String nickname;
    @ApiModelProperty(required = true, value = "유저 아이디", example = "test1234")
    private String userId;
    @ApiModelProperty(required = true, value = "유저 이메일", example = "test1234@naver.com")
    private String email;
    @ApiModelProperty(required = true, value = "유저 패스워드", example = "password1234")
    private String password;
    @ApiModelProperty(required = true, value = "유저 연락처", example = "01012345678")
    private String telephone;
    @ApiModelProperty(required = true, value = "유저 관심 키워드", example = "화려한,유화")
    private List<String> keywords;

}
