package com.sptp.backend.auction.event;

import com.sptp.backend.art_work.repository.ArtWork;
import com.sptp.backend.art_work.repository.ArtWorkRepository;
import com.sptp.backend.auction.repository.Auction;
import com.sptp.backend.bidding.repository.Bidding;
import com.sptp.backend.bidding.repository.BiddingRepository;
import com.sptp.backend.common.NotificationCode;
import com.sptp.backend.member.repository.Member;
import com.sptp.backend.notification.repository.Notification;
import com.sptp.backend.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Async
@Transactional
@Component
@RequiredArgsConstructor
public class    AuctionEventListener {

    private final NotificationRepository notificationRepository;
    private final ArtWorkRepository artWorkRepository;
    private final BiddingRepository biddingRepository;

    // 경매 등록 알림(판매자), 전시회 등록 알림(판매자), 작품 낙찰 성공(판매자,구매자), 작품 유찰 알림(판매자)
    @EventListener
    public void handleAuctionEvent(AuctionEvent auctionEvent) {

        NotificationCode notificationCode = auctionEvent.getNotificationCode();
        ArtWork artWork = auctionEvent.getArtWork();

        noticeToSeller(artWork, notificationCode);

        if(notificationCode.equals(NotificationCode.SUCCESSFUL_BID)) {
            noticeToBuyer(artWork, notificationCode);
        }
    }

    // 판매자
    public void noticeToSeller(ArtWork artWork, NotificationCode notificationCode) {

        saveNotification(artWork.getMember(), artWork, notificationCode);
    }

    // 구매자
    public void noticeToBuyer(ArtWork artWork, NotificationCode notificationCode) {

        Optional<Bidding> bidding = biddingRepository.getFirstByArtWorkOrderByPriceDesc(artWork);
        if(bidding.isPresent()) {
            saveNotification(bidding.get().getMember(), artWork, notificationCode);
        }
    }

    public void saveNotification(Member member, ArtWork artWork, NotificationCode notificationCode) {

        Notification notification = Notification.builder()
                .member(member)
                .title(notificationCode.getTitle())
                .message(artWork.getTitle() + notificationCode.getMessage())
                .details(notificationCode.getDetails())
                .data(artWork.getId())
                .checked(false)
                .build();

        notificationRepository.save(notification);
    }
}
