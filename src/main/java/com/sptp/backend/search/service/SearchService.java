package com.sptp.backend.search.service;

import com.sptp.backend.common.exception.CustomException;
import com.sptp.backend.common.exception.ErrorCode;
import com.sptp.backend.member.repository.Member;
import com.sptp.backend.member_recent_search.repository.MemberRecentSearch;
import com.sptp.backend.member_recent_search.repository.MemberRecentSearchRepository;
import com.sptp.backend.search.web.response.SearchWordResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final MemberRecentSearchRepository memberRecentSearchRepository;

    @Transactional
    public void saveSearchWord(Member member, String word) {

        // 최근 검색어 데이터에 없을 경우
        if (!memberRecentSearchRepository.existsByWord(word)) {
            memberRecentSearchRepository.save(MemberRecentSearch.builder().member(member).word(word).build());
        }
    }

    @Transactional
    public void deleteSearchWord(Long wordId) {

        MemberRecentSearch findMemberRecentSearch = memberRecentSearchRepository.findById(wordId)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NOT_FOUND_SEARCH_WORD);
        });

        memberRecentSearchRepository.delete(findMemberRecentSearch);
    }

    @Transactional
    public void deleteSearchWordAll(Member member) {

        memberRecentSearchRepository.deleteByMember(member);
    }

    @Transactional(readOnly = true)
    public List<SearchWordResponse> getRecentSearchWord(Member member) {

        List<MemberRecentSearch> memberRecentSearches = memberRecentSearchRepository.findTop10ByMemberOrderByIdDesc(member);

        return memberRecentSearches.stream().map(m -> new SearchWordResponse(m.getId(), m.getWord())).collect(Collectors.toList());
    }
}
