package com.sptp.backend.member_preferred_artist.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sptp.backend.member.repository.Member;
import lombok.RequiredArgsConstructor;
import static com.sptp.backend.member_preferred_artist.repository.QMemberPreferredArtist.*;
import static com.sptp.backend.member.repository.QMember.*;

import java.util.List;

@RequiredArgsConstructor
public class MemberPreferredArtistCustomRepositoryImpl implements MemberPreferredArtistCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Member> findPreferredArtist(Long memberId) {
        return queryFactory
                .select(member)
                .from(member, memberPreferredArtist)
                .where(member.id.eq(memberPreferredArtist.artist.id), memberPreferredArtist.member.id.eq(memberId))
                .fetch();
    }

    @Override
    public List<Member> findCertificationList() {
        return queryFactory
                .select(member)
                .from(member)
                .where(member.certificationImage.isNotNull(), member.roles.contains("ROLE_USER"))
                .fetch();
    }
}
