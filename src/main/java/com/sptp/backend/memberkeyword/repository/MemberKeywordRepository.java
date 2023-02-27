package com.sptp.backend.memberkeyword.repository;

import com.sptp.backend.member.repository.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberKeywordRepository extends JpaRepository<MemberKeyword, Long>, MemberKeywordCustomRepository {
    List<MemberKeyword> findByMemberId(Long memberId);

    void deleteByMember(Member member);
}
