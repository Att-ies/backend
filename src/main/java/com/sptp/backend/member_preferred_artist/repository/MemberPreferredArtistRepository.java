package com.sptp.backend.member_preferred_artist.repository;

import com.sptp.backend.member.repository.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberPreferredArtistRepository extends JpaRepository<MemberPreferredArtist, Long> {
    boolean existsByMember(Member member);
    boolean existsByArtist(Member artist);
}