package com.sptp.backend.common.Factory;

import com.sptp.backend.art_work.repository.ArtWork;
import com.sptp.backend.auction.repository.Auction;
import com.sptp.backend.auction.repository.AuctionStatus;
import com.sptp.backend.member.repository.Member;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ArtWorkFactory {

    private static Long id = 1L;

    public ArtWork createArtWork(Member artist, Auction auction) {
        return ArtWork.builder()
                .id(id++)
                .member(artist)
                .auction(auction)
                .genre("회화")
                .title("콰야 녹아내리는 고드름")
                .material("Oil On Canvas")
                .price(1000000L)
                .build();
    }
}