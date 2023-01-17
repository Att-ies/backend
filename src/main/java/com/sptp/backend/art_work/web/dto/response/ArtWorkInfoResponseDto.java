package com.sptp.backend.art_work.web.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtWorkInfoResponseDto {

    private String title;
    private String artistEducation;
    private String artistName;
    private Integer productionYear;
    private String material;
    private String genre;
    private boolean frame;
    private Integer width;
    private Integer length;
    private Integer height;
    private Integer size;
    private String description;

    private String guaranteeImage;
    private String mainImage;
    private String artistImage;
    private List<String> keywords;
    private List<String> images;

}
