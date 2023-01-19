package com.sptp.backend.auction.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class AuctionCustomRepositoryImpl implements AuctionCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Auction> findLatestScheduledAuction() {
        return queryFactory
                .select(QAuction.auction)
                .from(QAuction.auction)
                .where(QAuction.auction.status.eq(AuctionStatus.SCHEDULED.getType()))
                .orderBy(QAuction.auction.startDate.asc())
                .fetch();
    }
}
