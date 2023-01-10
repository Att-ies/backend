package com.sptp.backend.member_preferred_artist.repository;

import com.sptp.backend.member.repository.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberPreferredArtistRepository extends JpaRepository<MemberPreferredArtist, Long> {
}