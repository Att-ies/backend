package com.sptp.backend.memberkeyword.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberKeywordRepository extends JpaRepository<MemberKeyword, Long>, MemberKeywordCustomRepository {
}
