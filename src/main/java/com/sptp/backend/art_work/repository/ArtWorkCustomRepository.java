package com.sptp.backend.art_work.repository;

public interface ArtWorkCustomRepository {

    void updateStatusToProcessing(Long auctionId);

    void updateStatusToTerminated(Long auctionId);
}
