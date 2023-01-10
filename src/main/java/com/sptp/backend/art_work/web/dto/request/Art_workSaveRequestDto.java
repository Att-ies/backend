package com.sptp.backend.art_work.web.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Art_workSaveRequestDto {

    private MultipartFile[] image;
    private MultipartFile guaranteeImage;
    private String title;
    private String[] keywords;
    private String productionYear;
    private String material;
    private String size;
    private Integer price;
    private String status;
    private String status_description;

}
