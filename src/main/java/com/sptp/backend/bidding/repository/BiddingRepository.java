package com.sptp.backend.bidding.repository;

import com.sptp.backend.art_work.repository.ArtWork;
import com.sptp.backend.member.repository.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BiddingRepository extends JpaRepository<Bidding, Long>, BiddingCustomRepository {

    Optional<Bidding> findByArtWorkAndMember(ArtWork artWork, Member member);

    Optional<Bidding> getFirstByArtWorkOrderByPriceDesc(ArtWork artWork);

    boolean existsByArtWorkId(Long artWorkId);

    @EntityGraph(attributePaths = {"member"})
    List<Bidding> findAllByArtWorkOrderByPriceDesc(ArtWork artWork);

}
