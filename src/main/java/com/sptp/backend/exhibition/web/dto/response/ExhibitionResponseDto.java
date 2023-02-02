package com.sptp.backend.exhibition.web.dto.response;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExhibitionResponseDto {

    private String title;
    private String education;
    private String description;
    private String genre;
    private String image;
}
