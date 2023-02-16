package com.sptp.backend.art_work_keyword.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtWorkKeywordRepository extends JpaRepository<ArtWorkKeyword, Long> {

    List<ArtWorkKeyword> findByArtWorkId(Long artWorkId);

    void deleteByArtWorkId(Long artWorkId);
}
