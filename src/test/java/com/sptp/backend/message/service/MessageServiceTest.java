package com.sptp.backend.message.service;

import com.sptp.backend.artwork.repository.ArtWork;
import com.sptp.backend.chat_room.repository.ChatRoom;
import com.sptp.backend.chat_room.repository.ChatRoomRepository;
import com.sptp.backend.common.exception.CustomException;
import com.sptp.backend.common.exception.ErrorCode;
import com.sptp.backend.member.repository.Member;
import com.sptp.backend.member.repository.MemberRepository;
import com.sptp.backend.message.repository.Message;
import com.sptp.backend.message.repository.MessageRepository;
import com.sptp.backend.message.web.dto.MessageRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @InjectMocks
    MessageService messageService;

    @Mock
    ChatRoomRepository chatRoomRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    MessageRepository messageRepository;


    @Nested
    class saveMessageTest {
        long senderId = 1L;
        long receiverId = 2L;
        long artWorkId = 4L;
        long chatRoomId = 4L;
        Member sender;
        Member receiver;
        ArtWork artWork;
        ChatRoom chatRoom;
        MessageRequest messageRequest;

        @BeforeEach
        void init() {
            sender = Member.builder()
                    .id(senderId)
                    .userId("sender")
                    .password("12345678")
                    .build();

            receiver = Member.builder()
                    .id(receiverId)
                    .userId("receiver")
                    .password("12345678")
                    .build();

            artWork = ArtWork.builder()
                    .id(artWorkId)
                    .build();

            chatRoom = ChatRoom.builder()
                    .id(chatRoomId)
                    .artist(sender)
                    .collector(receiver)
                    .artWork(artWork)
                    .build();

            messageRequest = MessageRequest.builder()
                    .chatRoomId(chatRoomId)
                    .message("test")
                    .build();
        }

        @Test
        void success() {
            //given
            when(memberRepository.findById(senderId))
                    .thenReturn(Optional.of(sender));

            when(chatRoomRepository.findById(chatRoomId))
                    .thenReturn(Optional.of(chatRoom));

            when(messageRepository.save(any(Message.class)))
                    .thenReturn(Message.builder().build());

            //when
            //then
            assertThatNoException().isThrownBy(() -> messageService.saveMessage(senderId, messageRequest));
        }

        @Test
        void failByNotFoundMember() {
            //given
            when(memberRepository.findById(senderId))
                    .thenReturn(Optional.empty());

            //when
            //then
            assertThatThrownBy(() -> messageService.saveMessage(senderId, messageRequest))
                    .isInstanceOf(CustomException.class)
                    .message().isEqualTo(ErrorCode.NOT_FOUND_MEMBER.getDetail());
        }

        @Test
        void failByNotFoundChatRoom() {
            //given
            when(memberRepository.findById(senderId))
                    .thenReturn(Optional.of(sender));

            when(chatRoomRepository.findById(chatRoomId))
                    .thenReturn(Optional.empty());

            //when
            //then
            assertThatThrownBy(() -> messageService.saveMessage(senderId, messageRequest))
                    .isInstanceOf(CustomException.class)
                    .message().isEqualTo(ErrorCode.NOT_FOUND_CHAT_ROOM.getDetail());
        }
    }
}