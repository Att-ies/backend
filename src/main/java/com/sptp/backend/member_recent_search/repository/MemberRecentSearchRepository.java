package com.sptp.backend.member_recent_search.repository;

import com.sptp.backend.member.repository.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRecentSearchRepository extends JpaRepository<MemberRecentSearch, Long> {

    boolean existsByWord(String word);

    void deleteByMember(Member member);

    // 최근 검색어 10개까지만 조회
    List<MemberRecentSearch> findTop10ByMemberOrderByIdDesc(Member member);
}
