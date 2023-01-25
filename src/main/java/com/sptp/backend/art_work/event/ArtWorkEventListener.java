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

import java.util.Optional;

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

        if(notificationCode.equals(NotificationCode.SAVE_ARTWORK) || notificationCode.equals(NotificationCode.SUGGEST_BID)){
            saveNotification(member, artWork, notificationCode);
        }
        if(notificationCode.equals(NotificationCode.STILL_BID)) {
            sendToFailure(artWork, notificationCode);
        }
    }

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

    public void saveNotification(Member member, ArtWork artWork, NotificationCode notificationCode){

        if(notificationCode.equals(NotificationCode.SUGGEST_BID)) {
            Optional<Bidding> bidding = biddingRepository.findByArtWorkAndMember(artWork, member);
            Notification notification = Notification.builder()
                    .member(member)
                    .title(notificationCode.getTitle())
                    .message(artWork.getTitle() + "을 " + bidding.get().getPrice() + notificationCode.getMessage())
                    .details(notificationCode.getDetails())
                    .build();

            notificationRepository.save(notification);
            return;
        }

        Notification notification = Notification.builder()
                .member(member)
                .title(notificationCode.getTitle())
                .message(artWork.getTitle() + notificationCode.getMessage())
                .details(notificationCode.getDetails())
                .build();

        notificationRepository.save(notification);

    }
}
