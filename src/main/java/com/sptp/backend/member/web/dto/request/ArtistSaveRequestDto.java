package com.sptp.backend.member.web.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistSaveRequestDto {

    private String nickname;
    private String userId;
    private String email;
    private String password;
    private String telephone;

    private String education;
    private String history;
    private String description;
    private String instagram;
    private String behance;

}
