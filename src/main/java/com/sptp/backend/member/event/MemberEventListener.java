package com.sptp.backend.member.event;

import com.sptp.backend.common.NotificationCode;
import com.sptp.backend.member.repository.Member;
import com.sptp.backend.notification.repository.Notification;
import com.sptp.backend.notification.repository.NotificationRepository;
import com.sptp.backend.notification.service.NotificationService;
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
    private final NotificationService notificationService;

    @EventListener
    public void handleMemberUpdateEvent(MemberUpdateEvent memberUpdateEvent){

        Member member = memberUpdateEvent.getMember();
        NotificationCode notificationCode = memberUpdateEvent.getNotificationCode();

        notificationService.saveNotification(member, notificationCode);
    }
}
