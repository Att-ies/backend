package com.sptp.backend.art_work.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sptp.backend.auction.repository.AuctionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.sptp.backend.art_work.repository.QArtWork.*;

@RequiredArgsConstructor
public class ArtWorkCustomRepositoryImpl implements ArtWorkCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public void updateStatusToProcessing(Long auctionId) {

        queryFactory
                .update(artWork)
                .set(artWork.saleStatus, ArtWorkStatus.PROCESSING.getType())
                .where(artWork.auction.id.eq(auctionId))
                .execute();
    }

    @Override
    public void updateStatusToTerminated(Long auctionId) {

        queryFactory
                .update(artWork)
                .set(artWork.saleStatus, ArtWorkStatus.SALES_SUCCESS.getType())
                .where(artWork.auction.id.eq(auctionId))
                .execute();
    }

    @Override
    public List<ArtWork> findTerminatedAuctionArtWorkList(Long auctionId, Long artWorkId, Pageable pageable) {
        List<ArtWork> results =  queryFactory
                .select(artWork)
                .from(artWork)
                .where(
                        ltArtWorkId(artWorkId),
                        artWork.auction.id.eq(auctionId),
                        artWork.saleStatus.ne(ArtWorkStatus.REGISTERED.getType()),
                        artWork.saleStatus.ne(ArtWorkStatus.PROCESSING.getType())
                )
                .orderBy(artWork.id.desc())
                .limit(pageable.getPageSize()+1)
                .fetch();

        return results;
    }

    private BooleanExpression ltArtWorkId(Long artWorkId) {

        if (artWorkId == null) {
            return null;
        }

        return artWork.id.lt(artWorkId);
    }
}
