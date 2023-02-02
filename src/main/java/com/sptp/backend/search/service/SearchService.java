package com.sptp.backend.search.service;

import com.sptp.backend.art_work.repository.ArtWork;
import com.sptp.backend.art_work.repository.ArtWorkRepository;
import com.sptp.backend.common.exception.CustomException;
import com.sptp.backend.common.exception.ErrorCode;
import com.sptp.backend.member.repository.Member;
import com.sptp.backend.member_preffereed_art_work.repository.MemberPreferredArtWorkRepository;
import com.sptp.backend.member_recent_search.repository.MemberRecentSearch;
import com.sptp.backend.member_recent_search.repository.MemberRecentSearchRepository;
import com.sptp.backend.search.web.response.SearchArtWorkResponse;
import com.sptp.backend.search.web.response.SearchWordResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final MemberRecentSearchRepository memberRecentSearchRepository;
    private final ArtWorkRepository artWorkRepository;
    private final MemberPreferredArtWorkRepository memberPreferredArtWorkRepository;

    @Value("${aws.storage.url}")
    private String storageUrl;

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

    @Transactional(readOnly = true)
    public List<SearchArtWorkResponse> getSearchArtWorks(Member member, String word) {

        List<SearchArtWorkResponse> searchArtWorkResponseList = new ArrayList<>();
        List<ArtWork> artWorkList = artWorkRepository.findBySearchWord(word);

        for (ArtWork artWork : artWorkList) {

            boolean pick = false;

            if (memberPreferredArtWorkRepository.existsByMemberAndArtWork(member, artWork)) {
                pick = true;
            }

            searchArtWorkResponseList.add(SearchArtWorkResponse.builder()
                    .id(artWork.getId())
                    .image(storageUrl + artWork.getMainImage())
                    .artistName(artWork.getMember().getNickname())
                    .title(artWork.getTitle())
                    .education(artWork.getMember().getEducation())
                    .pick(pick)
                    .build());
        }
        return searchArtWorkResponseList;
    }
}
