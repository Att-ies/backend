package com.sptp.backend.common.Factory;

import com.sptp.backend.auction.repository.Auction;
import com.sptp.backend.auction.repository.AuctionStatus;
import com.sptp.backend.member.repository.Member;
import com.sptp.backend.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;

@Component
public class AuctionFactory {

    private static Long id = 1L;

    public Auction createTerminatedAuction() {
        LocalDateTime now = LocalDateTime.now();
        return Auction.builder()
                .id(id++)
                .status(AuctionStatus.TERMINATED.getType())
                .startDate(now.minusWeeks(3))
                .endDate(now.minusWeeks(2))
                .turn(1)
                .build();
    }

    public Auction createProcessingAuction() {
        LocalDateTime now = LocalDateTime.now();
        return Auction.builder()
                .id(id++)
                .status(AuctionStatus.PROCESSING.getType())
                .startDate(now.minusDays(1))
                .endDate(now.plusWeeks(1))
                .turn(2)
                .build();
    }

    public Auction createScheduledAuction() {
        LocalDateTime now = LocalDateTime.now();
        return Auction.builder()
                .id(id++)
                .status(AuctionStatus.SCHEDULED.getType())
                .startDate(now.plusWeeks(1))
                .endDate(now.plusWeeks(2))
                .turn(3)
                .build();
    }
}