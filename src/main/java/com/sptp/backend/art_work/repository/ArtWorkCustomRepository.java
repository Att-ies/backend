package com.sptp.backend.art_work.repository;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArtWorkCustomRepository {

    void updateStatusToProcessing(Long auctionId);

    List<ArtWork> findTerminatedAuctionArtWorkList(Long auctionId, Long artWorkId, Pageable pageable);
}
