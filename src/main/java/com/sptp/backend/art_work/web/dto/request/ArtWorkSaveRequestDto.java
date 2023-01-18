package com.sptp.backend.art_work.web.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtWorkSaveRequestDto {

    private MultipartFile[] image;
    private MultipartFile guaranteeImage;
    private String title;
    private String[] keywords;
    private Integer productionYear;
    private String material;
    private String status;
    private String statusDescription;
    private Integer width;
    private Integer length;
    private Integer height;
    private Integer size;
    private boolean frame;
    private String genre;
    private Integer price;
    private String description;

}
