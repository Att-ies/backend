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
public class AuctionStartScheduler {

    private final AuctionService auctionService;

    public void executeScheduler(LocalDateTime startDate, LocalDateTime endDate, Integer turn) {

        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                log.info("[log] 경매 {}회차 시작", turn);
                auctionService.startAuction(turn);
            }
        };

        Timer jobScheduler = new Timer();

        Duration duration = Duration.between(LocalDateTime.now(), startDate);

        jobScheduler.schedule(timerTask, duration.toSeconds()*1000);

        log.info("[log] 경매 {}회차 시작 예약, {}초 이후 실행", turn, duration.toSeconds());
    }
}
