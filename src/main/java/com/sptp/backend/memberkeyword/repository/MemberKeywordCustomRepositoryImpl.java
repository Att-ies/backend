package com.sptp.backend.memberkeyword.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.sptp.backend.memberkeyword.repository.QMemberKeyword.*;

@RequiredArgsConstructor
public class MemberKeywordCustomRepositoryImpl implements MemberKeywordCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public void deleteByMemberId(Long memberId) {
        queryFactory.delete(memberKeyword)
                .where(memberKeyword.member.id.eq(memberId))
                .execute();
    }
}
