package com.sptp.backend.art_work.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sptp.backend.common.KeywordMap;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.sptp.backend.art_work.repository.QArtWork.*;
import static com.sptp.backend.art_work_keyword.repository.QArtWorkKeyword.*;
import static com.sptp.backend.member.repository.QMember.*;
import static com.sptp.backend.bidding.repository.QBidding.*;

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

    @Override
    public List<ArtWork> findDeliveryArtWorkList(Long auctionId) {

        List<ArtWork> results = queryFactory
                .select(artWork)
                .from(artWork)
                .innerJoin(artWork.member, member)
                .where(
                        artWork.auction.id.eq(auctionId),
                        artWork.saleStatus.eq(ArtWorkStatus.SALES_SUCCESS.getType())
                )
                .fetch();

        return results;
    }

    @Override
    public List<ArtWork> findBySearchWord(String word) {

        // 검색어가 서버에 정의된 관심 키워드일 경우에는 키워드를 통한 검색
        if (KeywordMap.map.containsKey(word)) {

            return queryFactory
                    .select(artWork)
                    .from(artWork, artWorkKeyword)
                    .join(artWork)
                    .where(
                            artWork.id.eq(artWorkKeyword.artWork.id),
                            artWorkKeyword.keywordId.in(KeywordMap.map.get(word)),
                            artWork.saleStatus.ne(ArtWorkStatus.REGISTERED.getType())
                    )
                    .fetch();
        }

        // 그 외에는 제목, 작가, 장르를 통한 검색
        return queryFactory
                .select(artWork)
                .from(artWork)
                .join(artWork)
                .where(artWork.member.nickname.contains(word)
                        .or(artWork.title.contains(word))
                        .or(artWork.genre.contains(word))
                        .and(artWork.saleStatus.ne(ArtWorkStatus.REGISTERED.getType()))
                )
                .fetch();
    }

    private BooleanExpression ltArtWorkId(Long artWorkId) {

        if (artWorkId == null) {
            return null;
        }

        return artWork.id.lt(artWorkId);
    }
}
