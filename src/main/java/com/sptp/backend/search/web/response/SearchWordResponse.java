package com.sptp.backend.search.web.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchWordResponse {

    private Long id;
    private String word;
}
