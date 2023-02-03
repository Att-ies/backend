package com.sptp.backend.message.service;

import com.sptp.backend.chat_room.repository.ChatRoom;
import com.sptp.backend.chat_room_connection.repository.ChatRoomConnectionRepository;
import com.sptp.backend.chat_room.repository.ChatRoomRepository;
import com.sptp.backend.common.NotificationCode;
import com.sptp.backend.common.exception.CustomException;
import com.sptp.backend.common.exception.ErrorCode;
import com.sptp.backend.member.event.MemberEvent;
import com.sptp.backend.member.repository.Member;
import com.sptp.backend.member.repository.MemberRepository;
import com.sptp.backend.message.event.MessageEvent;
import com.sptp.backend.message.repository.Message;
import com.sptp.backend.message.repository.MessageRepository;
import com.sptp.backend.message.web.dto.MessageRequest;
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
    private final ApplicationEventPublisher eventPublisher;

    public void saveMessage(MessageRequest messageRequest) {

        Member sender = memberRepository.findById(messageRequest.getSenderId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        ChatRoom chatRoom = chatRoomRepository.findById(messageRequest.getChatRoomId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHAT_ROOM));

        Message message = Message.builder()
                .sender(sender)
                .chatRoom(chatRoom)
                .message(messageRequest.getMessage())
                .isRead(isOtherConnecting(messageRequest)) // 상대방이 접속 상태면 읽음 처리
                .build();

        messageRepository.save(message);

        eventPublisher.publishEvent(new MessageEvent(chatRoom, message, NotificationCode.CHATTING));
    }

    private boolean isOtherConnecting(MessageRequest messageRequest) {

        int connectionCount = chatRoomConnectionRepository.countByChatRoomId(messageRequest.getChatRoomId());
        return connectionCount == 2; // 발송자와 상대방이 접속한 경우
    }
}
