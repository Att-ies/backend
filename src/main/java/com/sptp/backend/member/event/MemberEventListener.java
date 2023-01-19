package com.sptp.backend.member.event;

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
public class MemberEventListener {

    private final NotificationRepository notificationRepository;

    @EventListener
    public void handleMemberToArtistEvent(MemberToArtistEvent memberToArtistEvent){

        Member member = memberToArtistEvent.getMember();

        saveNotification(member, NotificationCode.MEMBER_TO_ARTIST);
    }

    private void saveNotification(Member member, NotificationCode notificationCode){

        Notification notification = Notification.builder()
                .member(member)
                .title(notificationCode.getTitle())
                .message(notificationCode.getMessage())
                .build();

        notificationRepository.save(notification);
    }
}
