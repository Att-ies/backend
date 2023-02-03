package com.sptp.backend.message.event;

import com.sptp.backend.art_work.repository.ArtWork;
import com.sptp.backend.art_work.repository.ArtWorkRepository;
import com.sptp.backend.chat_room.repository.ChatRoom;
import com.sptp.backend.common.NotificationCode;
import com.sptp.backend.common.exception.CustomException;
import com.sptp.backend.common.exception.ErrorCode;
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
    private final ArtWorkRepository artWorkRepository;

    // 채팅 알림
    @EventListener
    public void handleMessageEvent(MessageEvent messageEvent) {

        NotificationCode notificationCode = messageEvent.getNotificationCode();
        ChatRoom chatRoom = messageEvent.getChatRoom();
        Message message = messageEvent.getMessage();
        ArtWork artWork = artWorkRepository.findById(chatRoom.getArtWork().getId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ARTWORK));

        // 해당 채팅방에 대한 알림이 이미 존재한다면
        if(notificationRepository.existsByChatRoomId(chatRoom.getId())) {
            updateNotification(chatRoom.getId(), artWork, notificationCode);
            return;
        }

        if(chatRoom.getArtist().getId().equals(message.getSender().getId())) {
            saveNotification(chatRoom.getMember(), artWork, chatRoom, notificationCode);
        }
        if(chatRoom.getMember().getId().equals(message.getSender().getId())) {
            saveNotification(chatRoom.getArtist(), artWork, chatRoom, notificationCode);
        }
    }

    public void saveNotification(Member member, ArtWork artWork, ChatRoom chatRoom, NotificationCode notificationCode) {

        Notification notification = Notification.builder()
                .member(member)
                .title(notificationCode.getTitle())
                .message(artWork.getTitle() + notificationCode.getMessage())
                .details(notificationCode.getDetails())
                .checked(false)
                .chatRoomId(chatRoom.getId())
                .build();

        notificationRepository.save(notification);
    }

    public void updateNotification(Long chatRoomId, ArtWork artWork, NotificationCode notificationCode) {

        Notification findNotification = notificationRepository.findByChatRoomId(chatRoomId)
                        .orElseThrow();

        // 작품 이름 변경된 거 반영할 겸 알림 업데이트. 실제론 modifiedDate 변경이 목표
        findNotification.updateMessage(artWork.getTitle() + notificationCode.getMessage());
    }
}