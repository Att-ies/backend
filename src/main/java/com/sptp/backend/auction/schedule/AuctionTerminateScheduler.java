package com.sptp.backend.auction.schedule;


import com.sptp.backend.auction.service.AuctionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuctionTerminateScheduler {

    private final AuctionService auctionService;

    public void executeScheduler(LocalDateTime startDate, LocalDateTime endDate, Integer turn) {

        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                log.info("[log] 경매 {}회차 종료", turn);
                auctionService.terminateAuction(turn);
            }
        };

        Timer jobScheduler = new Timer();

        Duration duration = Duration.between(LocalDateTime.now(), endDate);

        jobScheduler.schedule(timerTask, duration.toSeconds()*1000);

        log.info("[log] 경매 {}회차 종료 예약, {}초 이후 실행", turn, duration.toSeconds());
    }
}
