package com.sptp.backend.memberkeyword.repository;

import java.util.List;

public interface MemberKeywordCustomRepository {
    void deleteByMemberId(Long memberId);
    List<Integer> findKeywordIdByMemberId(Long memberId);
}
