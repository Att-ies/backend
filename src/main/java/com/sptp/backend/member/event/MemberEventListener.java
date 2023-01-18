package com.sptp.backend.member.event;

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
        log.info(member.getNickname() + " becomes artist.");

        saveNotification(member);
    }

    private void saveNotification(Member member){

        Notification notification = Notification.builder()
                .member(member)
                .title("작가 등록 완료")
                .message("아띠즈 공식 작가로 등록되었어요")
                .build();

        notificationRepository.save(notification);
    }
}
