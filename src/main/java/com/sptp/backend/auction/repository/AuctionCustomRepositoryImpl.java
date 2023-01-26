package com.sptp.backend.auction.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.sptp.backend.auction.repository.QAuction.*;

@RequiredArgsConstructor
public class AuctionCustomRepositoryImpl implements AuctionCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Auction> findLatestScheduledAuction() {
        return queryFactory
                .select(auction)
                .from(auction)
                .where(auction.status.eq(AuctionStatus.SCHEDULED.getType())
                        .and(auction.startDate.goe(LocalDateTime.now())))
                .orderBy(auction.startDate.asc())
                .fetch();
    }

    @Override
    public Auction findCurrentlyProcessingAuction() {
        return queryFactory
                .select(auction)
                .from(auction)
                .where(auction.status.eq(AuctionStatus.PROCESSING.getType())
                        .and(auction.startDate.loe(LocalDateTime.now()))
                        .and(auction.endDate.goe(LocalDateTime.now())))
                .fetchOne();
    }

}
