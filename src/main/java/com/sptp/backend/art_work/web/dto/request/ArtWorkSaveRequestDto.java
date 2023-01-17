package com.sptp.backend.art_work.web.dto.request;

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
    private String productionYear;
    private String material;
    private String status;
    private String statusDescription;
    private Integer width;
    private Integer length;
    private Integer height;
    private Integer size;
    private boolean isFrame;
    private String genre;
    private Integer price;
    private String description;

}
