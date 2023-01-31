package com.sptp.backend.bidding.repository;

import com.querydsl.core.Tuple;
import com.sptp.backend.member.repository.Member;

import java.util.List;

public interface BiddingCustomRepository {

    List<Tuple> findByMemberWithMaxBidding(Member member);
}
