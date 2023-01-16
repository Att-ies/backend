package com.sptp.backend.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sptp.backend.art_work.repository.ArtWork;
import lombok.RequiredArgsConstructor;
import static com.sptp.backend.art_work.repository.QArtWork.*;
import static com.sptp.backend.art_work_keyword.repository.QArtWorkKeyword.*;

import java.util.List;

@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ArtWork> findCustomizedArtWork(List<Integer> memberKeywordId){
        return queryFactory
                .select(artWork)
                .from(artWork, artWorkKeyword)
                .where(artWork.id.eq(artWorkKeyword.artWork.id), artWorkKeyword.keywordId.in(memberKeywordId))
                .groupBy(artWork.id)
                .orderBy(artWork.count().desc())
                .fetch();
    }
}
