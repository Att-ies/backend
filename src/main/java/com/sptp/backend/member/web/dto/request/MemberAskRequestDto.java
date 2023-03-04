package com.sptp.backend.member.web.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberAskRequestDto {

    @ApiModelProperty(required = true, value = "문의 글 제목", example = "제목")
    private String title;
    @ApiModelProperty(required = true, value = "문의 글 내용", example = "내용")
    private String content;
    @ApiModelProperty(required = true, value = "문의 글 첨부 사진")
    private MultipartFile[] image;
}
