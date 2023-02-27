package com.sptp.backend.message.service;

import com.sptp.backend.art_work.repository.ArtWork;
import com.sptp.backend.chat_room.repository.ChatRoom;
import com.sptp.backend.chat_room_connection.repository.ChatRoomConnectionRepository;
import com.sptp.backend.chat_room.repository.ChatRoomRepository;
import com.sptp.backend.common.exception.CustomException;
import com.sptp.backend.common.exception.ErrorCode;
import com.sptp.backend.member.repository.Member;
import com.sptp.backend.member.repository.MemberRepository;
import com.sptp.backend.message.repository.Message;
import com.sptp.backend.message.repository.MessageRepository;
import com.sptp.backend.message.web.dto.MessageRequest;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @InjectMocks
    MessageService messageService;

    @Mock
    ChatRoomRepository chatRoomRepository;

    @Mock
    ChatRoomConnectionRepository chatRoomConnectionRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    MessageRepository messageRepository;


    @Nested
    class saveMessageTest {
        long senderId = 1L;
        long receiverId = 2L;
        long artWorkId = 3L;
        long chatRoomId = 4L;
        Member sender;
        Member receiver;
        ArtWork artWork;
        ChatRoom chatRoom;

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
                    .member(receiver)
                    .artWork(artWork)
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

            when(chatRoomConnectionRepository.countByChatRoomId(chatRoomId))
                    .thenReturn(2);

            //when
            //then
            assertThatNoException().isThrownBy(() -> messageService.saveMessage(senderId, chatRoomId, "message"));
        }

        @Test
        void failByNotFoundMember() {
            //given
            when(memberRepository.findById(senderId))
                    .thenReturn(Optional.empty());

            //when
            //then
            assertThatThrownBy(() -> messageService.saveMessage(senderId, chatRoomId, "message"))
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
            assertThatThrownBy(() -> messageService.saveMessage(senderId, chatRoomId, "message"))
                    .isInstanceOf(CustomException.class)
                    .message().isEqualTo(ErrorCode.NOT_FOUND_CHAT_ROOM.getDetail());
        }
    }
}