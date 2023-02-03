package com.sptp.backend.message.event;

import com.sptp.backend.chat_room.repository.ChatRoom;
import com.sptp.backend.common.NotificationCode;
import com.sptp.backend.member.event.MemberEvent;
import com.sptp.backend.member.repository.Member;
import com.sptp.backend.message.repository.Message;
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
public class MessageEventListener {

    private final NotificationRepository notificationRepository;

    // 채팅 알림
    @EventListener
    public void handleMemberEvent(MessageEvent messageEvent) {

        NotificationCode notificationCode = messageEvent.getNotificationCode();
        ChatRoom chatRoom = messageEvent.getChatRoom();
        Message message = messageEvent.getMessage();

        // 기존 data 값들과 현재 chatRoomId 비교해서, 이미 존재하는 채팅방 알림인지 검사


        if(chatRoom.getArtist().equals(message.getSender())) {
            saveNotification(chatRoom.getMember(), chatRoom, notificationCode);
        }
        if(chatRoom.getMember().equals(message.getSender())) {
            saveNotification(chatRoom.getArtist(), chatRoom, notificationCode);
        }
    }

    public void saveNotification(Member member, ChatRoom chatRoom, NotificationCode notificationCode) {

        Notification notification = Notification.builder()
                .member(member)
                .title(notificationCode.getTitle())
                .message(notificationCode.getMessage())
                .details(notificationCode.getDetails())
                .data(chatRoom.getId())
                .checked(false)
                .build();

        notificationRepository.save(notification);
    }
}
