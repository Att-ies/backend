package com.sptp.backend.auction.schedule;

import com.sptp.backend.auction.repository.Auction;
import com.sptp.backend.auction.repository.AuctionRepository;
import com.sptp.backend.auction.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final AuctionRepository auctionRepository;
    private final AuctionService auctionService;

    @PostConstruct
    public void schedulerInit() {

        List<Auction> auctionList = auctionRepository.findAll();

        for (Auction auction : auctionList) {

            LocalDateTime startDate = auction.getStartDate();
            LocalDateTime endDate = auction.getEndDate();
            Integer turn = auction.getTurn();

            if (auction.getStatus().equals("scheduled")) {

                AuctionStartScheduler auctionStartScheduler = new AuctionStartScheduler(auctionService);
                auctionStartScheduler.executeScheduler(startDate, endDate, turn);

                AuctionTerminateScheduler auctionTerminateScheduler = new AuctionTerminateScheduler(auctionService);
                auctionTerminateScheduler.executeScheduler(startDate, endDate, turn);
            }

            if (auction.getStatus().equals("processing")) {

                AuctionTerminateScheduler auctionTerminateScheduler = new AuctionTerminateScheduler(auctionService);
                auctionTerminateScheduler.executeScheduler(startDate, endDate, turn);
            }

        }
    }
}
