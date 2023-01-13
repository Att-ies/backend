package com.sptp.backend.member_preffereed_art_work.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sptp.backend.art_work.repository.ArtWork;
import com.sptp.backend.art_work.repository.QArtWork;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MemberPreferredArtWorkCustomRepositoryImpl implements MemberPreferredArtWorkCustomRepository{

    private final JPAQueryFactory queryFactory;
    QMemberPreferredArtWork preferredArtWork = QMemberPreferredArtWork.memberPreferredArtWork;
    QArtWork artWork = QArtWork.artWork;

    @Override
    public List<ArtWork> findPreferredArtWork(Long memberId){
        return queryFactory
                .select(artWork)
                .from(artWork, preferredArtWork)
                .where(artWork.id.eq(preferredArtWork.artWork.id), preferredArtWork.member.id.eq(memberId))
                .fetch();
    }
}
