package com.sptp.backend.member.web.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistUpdateRequest {

    @ApiModelProperty(required = true, value = "유저 닉네임", example = "아라")
    private String nickname;
    @ApiModelProperty(required = true, value = "유저 이메일", example = "test1234@naver.com")
    private String email;
    @ApiModelProperty(required = true, value = "학력", example = "xx대학교 xx학과")
    private String education;
    @ApiModelProperty(required = true, value = "이력", example = "이력입니다.")
    private String history;
    @ApiModelProperty(required = true, value = "소개,설명", example = "소개글입니다.")
    private String description;
    @ApiModelProperty(required = true, value = "인스타그램 주소", example = "www.instagram.com")
    private String instagram;
    @ApiModelProperty(required = true, value = "비헨스 주소", example = "www.behance.com")
    private String behance;
    @ApiModelProperty(required = true, value = "유저 주소", example = "서울시 xx구 xx동 123동 1234호")
    private String address;
    @ApiModelProperty(required = true, value = "유저 연락처", example = "01012345678")
    private String telephone;
    @ApiModelProperty(required = true, value = "이미지 변경 여부", example = "true")
    private Boolean isChanged;
}
