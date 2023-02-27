package com.sptp.backend.bidding.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sptp.backend.member.repository.Member;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.sptp.backend.bidding.repository.QBidding.*;

@RequiredArgsConstructor
public class BiddingCustomRepositoryImpl implements BiddingCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Tuple> findByMemberWithMaxBidding(Member member) {
        return queryFactory
                .select(bidding.artWork, bidding.price.max())
                .from(bidding)
                .where(bidding.member.id.eq(member.getId()))
                .groupBy(bidding.artWork.id)
                .fetch();
    }
}
