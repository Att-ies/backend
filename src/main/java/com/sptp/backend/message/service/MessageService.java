package com.sptp.backend.message.service;

import com.sptp.backend.aws.service.FileManager;
import com.sptp.backend.chat_room.repository.ChatRoom;
import com.sptp.backend.chat_room_connection.repository.ChatRoomConnectionRepository;
import com.sptp.backend.chat_room.repository.ChatRoomRepository;
import com.sptp.backend.common.NotificationCode;
import com.sptp.backend.common.exception.CustomException;
import com.sptp.backend.common.exception.ErrorCode;
import com.sptp.backend.member.repository.Member;
import com.sptp.backend.member.repository.MemberRepository;
import com.sptp.backend.message.event.MessageEvent;
import com.sptp.backend.message.repository.Message;
import com.sptp.backend.message.repository.MessageRepository;
import com.sptp.backend.message.repository.MessageType;
import com.sptp.backend.message.web.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomConnectionRepository chatRoomConnectionRepository;
    private final MemberRepository memberRepository;
    private final FileManager fileManager;
    private final ApplicationEventPublisher eventPublisher;

    public MessageResponse saveMessage(Long senderId, Long chatRoomId, String textMessage) {

        ChatRoom chatRoom = getChatRoomOrThrow(chatRoomId);
        int connectionCount = chatRoomConnectionRepository.countByChatRoomId(chatRoomId);

        Message message = Message.builder()
                .sender(getMemberOrThrow(senderId))
                .chatRoom(chatRoom)
                .type(MessageType.TEXT.name())
                .content(textMessage)
                .isRead(isOtherConnecting(connectionCount)) // 상대방이 접속 상태면 읽음 처리
                .build();

        messageRepository.save(message);

        if (!isOtherConnecting(connectionCount)) {
            eventPublisher.publishEvent(new MessageEvent(chatRoom, message, NotificationCode.CHATTING));
        }

        return MessageResponse.builder()
                .chatRoomId(chatRoomId)
                .type(MessageType.TEXT.name())
                .content(textMessage)
                .build();
    }

    private boolean isOtherConnecting(int connectionCount) {

        return connectionCount == 2; // 발송자와 상대방이 접속한 경우
    }

    public MessageResponse saveImage(Long senderId, Long chatRoomId, String encodedImage) {

        ChatRoom chatRoom = getChatRoomOrThrow(chatRoomId);
        int connectionCount = chatRoomConnectionRepository.countByChatRoomId(chatRoomId);
        String storeImageFileName = fileManager.storeImageFile(encodedImage);

        Message message = Message.builder()
                .sender(getMemberOrThrow(senderId))
                .chatRoom(getChatRoomOrThrow(chatRoomId))
                .type(MessageType.IMAGE.name())
                .content(storeImageFileName)
                .isRead(isOtherConnecting(connectionCount)) // 상대방이 접속 상태면 읽음 처리
                .build();

        messageRepository.save(message);

        if (!isOtherConnecting(connectionCount)) {
            eventPublisher.publishEvent(new MessageEvent(chatRoom, message, NotificationCode.CHATTING));
        }

        return MessageResponse.builder()
                .chatRoomId(chatRoomId)
                .type(MessageType.IMAGE.name())
                .content(fileManager.getFullPath(storeImageFileName))
                .build();
    }

    private Member getMemberOrThrow(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
    }

    private ChatRoom getChatRoomOrThrow(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHAT_ROOM));
    }
}
