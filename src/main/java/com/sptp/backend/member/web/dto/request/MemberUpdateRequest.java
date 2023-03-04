package com.sptp.backend.member.web.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberUpdateRequest {

    @ApiModelProperty(required = true, value = "유저 닉네임", example = "아라")
    private String nickname;
    @ApiModelProperty(required = true, value = "유저 이메일", example = "test1234@naver.com")
    private String email;
    @ApiModelProperty(required = true, value = "유저 주소", example = "서울시 xx구 xx동 123동 1234호")
    private String address;
    @ApiModelProperty(required = true, value = "유저 연락처", example = "01012345678")
    private String telephone;
    @ApiModelProperty(required = true, value = "이미지 변경 여부", example = "true")
    private Boolean isChanged;
}