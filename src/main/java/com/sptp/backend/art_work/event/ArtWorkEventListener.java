package com.sptp.backend.art_work.event;

import com.sptp.backend.art_work.repository.ArtWork;
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

@Slf4j
@Async
@Transactional
@Component
@RequiredArgsConstructor
public class ArtWorkEventListener {

    private final NotificationRepository notificationRepository;
    private final BiddingRepository biddingRepository;

    // 작품 등록 완료(판매자), 입찰 알림(구매자), 입찰 경쟁 알림(구매실패자)
    @EventListener
    public void handleArtWorkEvent(ArtWorkEvent artWorkEvent){

        NotificationCode notificationCode = artWorkEvent.getNotificationCode();
        ArtWork artWork = artWorkEvent.getArtwork();
        Member member = artWorkEvent.getMember();
        Bidding bidding = artWorkEvent.getBidding();

        if(notificationCode.equals(NotificationCode.SAVE_ARTWORK) || notificationCode.equals(NotificationCode.SUGGEST_BID)){
            saveNotification(member, artWork, bidding, notificationCode);
        }
        if(notificationCode.equals(NotificationCode.STILL_BID)) {
            sendToFailure(member, artWork, notificationCode);
        }
    }

    public void sendToFailure(Member member, ArtWork artWork, NotificationCode notificationCode) {

        Iterable<Bidding> biddings = biddingRepository.findAllByArtWorkOrderByPriceDesc(artWork);

        for(Bidding bidding : biddings) {
            if(!member.getId().equals(bidding.getMember().getId())) {
                saveNotification(bidding.getMember(), artWork, bidding, notificationCode);
            }
        }
    }

    public void saveNotification(Member member, ArtWork artWork, Bidding bidding, NotificationCode notificationCode){

        if(notificationCode.equals(NotificationCode.SUGGEST_BID)) {
            Notification notification = Notification.builder()
                    .member(member)
                    .title(notificationCode.getTitle())
                    .message(artWork.getTitle() + "을 " + bidding.getPrice() + notificationCode.getMessage())
                    .details(notificationCode.getDetails())
                    .data(artWork.getId())
                    .checked(false)
                    .build();

            notificationRepository.save(notification);
            return;
        }

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
