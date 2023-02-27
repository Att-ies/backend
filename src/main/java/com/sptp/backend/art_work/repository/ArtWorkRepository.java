package com.sptp.backend.art_work.repository;

import com.sptp.backend.member.repository.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtWorkRepository extends JpaRepository<ArtWork, Long>, ArtWorkCustomRepository {

    List<ArtWork> findByMemberId(Long memberId);

    List<ArtWork> findArtWorkByMember(Member member);

    List<ArtWork> findByAuctionId(Long auctionId);

    List<ArtWork> findByAuctionIdAndSaleStatus(Long auctionId, String status);

    List<ArtWork> findByAuctionIdOrderByCreatedDateDesc(Long auctionId);

    Long countByAuctionId(Long auctionId);
}
