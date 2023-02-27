package com.sptp.backend.member.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberAskResponse {

    private Long id;
    private String title;
    private String content;
    private String answer;
    private String status;

    @JsonFormat(pattern = "yyyy.MM.dd HH:mm")
    private LocalDateTime date;
}
