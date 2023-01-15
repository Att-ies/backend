package com.sptp.backend.member_preffereed_art_work.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sptp.backend.art_work.repository.ArtWork;
import lombok.RequiredArgsConstructor;
import static com.sptp.backend.art_work.repository.QArtWork.*;
import static com.sptp.backend.member_preffereed_art_work.repository.QMemberPreferredArtWork.*;

import java.util.List;

@RequiredArgsConstructor
public class MemberPreferredArtWorkCustomRepositoryImpl implements MemberPreferredArtWorkCustomRepository{

    private final JPAQueryFactory queryFactory;
    
    @Override
    public List<ArtWork> findPreferredArtWork(Long memberId){
        return queryFactory
                .select(artWork)
                .from(artWork, memberPreferredArtWork)
                .where(artWork.id.eq(memberPreferredArtWork.artWork.id),memberPreferredArtWork.member.id.eq(memberId))
                .fetch();
    }
}
