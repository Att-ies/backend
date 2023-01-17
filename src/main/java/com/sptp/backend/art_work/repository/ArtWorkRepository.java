package com.sptp.backend.art_work.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtWorkRepository extends JpaRepository<ArtWork, Long> {

    List<ArtWork> findByMemberId(Long memberId);
}
