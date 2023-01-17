package com.sptp.backend.member.web.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberAskRequestDto {

    private String title;
    private String content;
    private MultipartFile[] image;
}
