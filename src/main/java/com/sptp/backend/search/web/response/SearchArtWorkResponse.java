package com.sptp.backend.search.web.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchArtWorkResponse {

    private Long id;
    private String image;
    private String title;
    private String artistName;
    private String education;
    private boolean pick;
}
