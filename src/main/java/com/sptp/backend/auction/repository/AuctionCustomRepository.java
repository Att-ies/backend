package com.sptp.backend.auction.repository;

import java.util.List;

public interface AuctionCustomRepository {

    List<Auction> findLatestScheduledAuction();

    Auction findCurrentlyProcessingAuction();
}
