package com.sptp.backend.auction.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionRepository extends JpaRepository<Auction, Long> {

    boolean existsByTurn(Integer turn);
}
