package com.sptp.backend.auction.event;

import com.sptp.backend.art_work.event.ArtWorkEvent;
import com.sptp.backend.art_work.repository.ArtWork;
import com.sptp.backend.art_work.repository.ArtWorkRepository;
import com.sptp.backend.auction.repository.Auction;
import com.sptp.backend.common.NotificationCode;
import com.sptp.backend.member.repository.Member;
import com.sptp.backend.member.repository.MemberRepository;
import com.sptp.backend.notification.repository.Notification;
import com.sptp.backend.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Async
@Transactional
@Component
@RequiredArgsConstructor
public class AuctionEventListener {

    private final NotificationRepository notificationRepository;
    private final ArtWorkRepository artWorkRepository;
    private final MemberRepository memberRepository;

    @EventListener
    public void handleAuctionEvent(AuctionEvent auctionEvent){

        NotificationCode notificationCode = auctionEvent.getNotificationCode();
        Auction auction = auctionEvent.getAuction();

        Iterable<ArtWork> artWorks = artWorkRepository.findByAuctionId(auction.getId());
        for(ArtWork artWork: artWorks){
            saveNotification(artWork.getMember(), artWork, notificationCode);
        }
    }

    public void saveNotification(Member member, ArtWork artWork, NotificationCode notificationCode){

        Notification notification = Notification.builder()
                .member(member)
                .title(notificationCode.getTitle())
                .message(artWork.getTitle() + notificationCode.getMessage())
                .details(notificationCode.getDetails())
                .data(artWork.getId())
                .build();

        notificationRepository.save(notification);
    }
}
