package com.sptp.backend.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sptp.backend.art_work.repository.ArtWork;
import com.sptp.backend.art_work.repository.ArtWorkStatus;
import lombok.RequiredArgsConstructor;

import static com.sptp.backend.art_work.repository.QArtWork.*;
import static com.sptp.backend.art_work_keyword.repository.QArtWorkKeyword.*;

import java.util.List;

@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ArtWork> findCustomizedArtWork(List<Integer> memberKeywordId, Integer page, Integer limit) {

        String[] saleStatus = {ArtWorkStatus.PROCESSING.getType(), ArtWorkStatus.SALES_SUCCESS.getType(), ArtWorkStatus.SALES_FAILED.getType()};

        return queryFactory
                .select(artWork)
                .from(artWork, artWorkKeyword)
                .where(artWork.id.eq(artWorkKeyword.artWork.id), artWorkKeyword.keywordId.in(memberKeywordId), artWork.saleStatus.in(saleStatus))
                .groupBy(artWork.id)
                .orderBy(artWork.count().desc(), artWork.createdDate.desc())
                .limit(limit + 1)
                .offset(limit * (page - 1))
                .fetch();
    }
}
