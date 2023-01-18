package com.sptp.backend.art_work_image.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtWorkImageRepository extends JpaRepository<ArtWorkImage, Long> {

    List<ArtWorkImage> findByArtWorkId(Long artWorkId);
}
