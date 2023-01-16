package com.sptp.backend.member_ask.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberAskRepository extends JpaRepository<MemberAsk, Long> {

    List<MemberAsk> findByMemberId(Long memberId);
}
