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
public class AuctionEventListener {

    private final NotificationRepository notificationRepository;
    private final ArtWorkRepository artWorkRepository;
    private final BiddingRepository biddingRepository;

    // 경매 등록 알림(작가), 전시회 등록 알림(작가), 작품 낙찰 성공(판매자,구매자), 작품 유찰 알림(판매실패자)
    @EventListener
    public void handleAuctionEvent(AuctionEvent auctionEvent) {

        NotificationCode notificationCode = auctionEvent.getNotificationCode();
        Auction auction = auctionEvent.getAuction();

        Iterable<ArtWork> artWorks = artWorkRepository.findByAuctionId(auction.getId());
        for(ArtWork artWork : artWorks) {

            if(notificationCode.equals(NotificationCode.SAVE_AUCTION) || notificationCode.equals(NotificationCode.SAVE_DISPLAY)) {
                sendToArtist(artWork.getMember(), artWork, notificationCode);
            }
            if(notificationCode.equals(NotificationCode.SUCCESSFUL_BID)) {
                sendToSeller(artWork.getMember(), artWork, notificationCode);
                sendToBuyer(artWork, notificationCode);
            }
            if(notificationCode.equals(NotificationCode.FAILED_BID)) {
                sendToFailure(artWork, notificationCode);
            }
        }
    }

    // 작가
    public void sendToArtist(Member member, ArtWork artWork, NotificationCode notificationCode) {

        
        saveNotification(member, artWork, notificationCode);
    }

    // 판매자
    public void sendToSeller(Member member, ArtWork artWork, NotificationCode notificationCode) {

        
    }

    // 구매자
    public void sendToBuyer(ArtWork artWork, NotificationCode notificationCode) {


    }

    // 구매실패자
    public void sendToFailure(ArtWork artWork, NotificationCode notificationCode) {

        Iterable<Bidding> biddings = biddingRepository.findAllByArtWorkOrderByPriceDesc(artWork);
        int index=0;
        for(Bidding bidding : biddings) {
            if(index!=0){
                saveNotification(bidding.getMember(), artWork, notificationCode);
            }
            index++;
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
