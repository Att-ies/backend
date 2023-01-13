package com.sptp.backend.member_preferred_artist.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sptp.backend.member.repository.Member;
import com.sptp.backend.member.repository.QMember;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MemberPreferredArtistCustomRepositoryImpl implements MemberPreferredArtistCustomRepository{

    private final JPAQueryFactory queryFactory;
    QMemberPreferredArtist preferredArtist = QMemberPreferredArtist.memberPreferredArtist;
    QMember member = QMember.member;

    @Override
    public List<Member> findPreferredArtist(Long memberId) {
        return queryFactory
                .select(member)
                .from(member, preferredArtist)
                .where(member.id.eq(preferredArtist.artist.id), preferredArtist.member.id.eq(memberId))
                .fetch();
    }
}
