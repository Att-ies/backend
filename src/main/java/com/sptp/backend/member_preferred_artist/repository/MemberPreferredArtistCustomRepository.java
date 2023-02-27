package com.sptp.backend.member_preferred_artist.repository;

import com.sptp.backend.member.repository.Member;

import java.util.List;

public interface MemberPreferredArtistCustomRepository {

    List<Member> findPreferredArtist(Long memberId);
    List<Member> findCertificationList();
}
