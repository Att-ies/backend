package com.sptp.backend.art_work.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sptp.backend.auction.repository.AuctionStatus;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ArtWorkCustomRepositoryImpl implements ArtWorkCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public void updateStatusToProcessing(Long auctionId) {

        queryFactory
                .update(QArtWork.artWork)
                .set(QArtWork.artWork.saleStatus, ArtWorkStatus.PROCESSING.getType())
                .where(QArtWork.artWork.auction.id.eq(auctionId))
                .execute();
    }

    @Override
    public void updateStatusToTerminated(Long auctionId) {

        queryFactory
                .update(QArtWork.artWork)
                .set(QArtWork.artWork.saleStatus, ArtWorkStatus.SALES_SUCCESS.getType())
                .where(QArtWork.artWork.auction.id.eq(auctionId))
                .execute();
    }
}
