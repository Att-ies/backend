package com.sptp.backend.art_work.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArtWorkSize {

    private Integer width;

    private Integer length;

    private Integer height;

    private Integer size;
}
