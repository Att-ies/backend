package com.sptp.backend.member_preffereed_art_work.repository;

import com.sptp.backend.art_work.repository.ArtWork;
import com.sptp.backend.member.repository.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberPreferredArtWorkRepository extends JpaRepository<MemberPreferredArtWork, Long>, MemberPreferredArtWorkCustomRepository {
    Boolean existsByMemberAndArtWork(Member member, ArtWork artWork);

    void deleteByMemberAndArtWork(Member member, ArtWork artWork);

    Long countByMemberId(Long memberId);
}
