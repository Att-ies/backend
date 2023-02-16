package com.sptp.backend.art_work.web.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtWorkEditRequestDto {

    private List<String> prevImage;
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
    private Long price;
    private String description;
}
