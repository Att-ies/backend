package com.sptp.backend.auction.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuctionRepository extends JpaRepository<Auction, Long> {

    boolean existsByTurn(Integer turn);

    Optional<Auction> findByTurn(Integer turn);
}
