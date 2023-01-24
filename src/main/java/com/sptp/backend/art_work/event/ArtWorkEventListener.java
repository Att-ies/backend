package com.sptp.backend.art_work.event;

import com.sptp.backend.art_work.repository.ArtWork;
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

    // 작품 등록 완료 알림, 경매 등록 알림, 전시회 등록 알림, 낙찰 알림
    @EventListener
    public void handleArtWorkEvent(ArtWorkEvent artWorkEvent){

        Member member = artWorkEvent.getMember();
        ArtWork artWork = artWorkEvent.getArtwork();
        NotificationCode notificationCode = artWorkEvent.getNotificationCode();

        saveNotification(member, artWork, notificationCode);
    }

    public void saveNotification(Member member, ArtWork artWork, NotificationCode notificationCode){

        Notification notification = Notification.builder()
                .member(member)
                .title(notificationCode.getTitle())
                .message(artWork.getTitle() + notificationCode.getMessage())
                .details(notificationCode.getDetails())
                .data(null)
                .build();

        notificationRepository.save(notification);
    }
}
