package com.sptp.backend.chat_room.service;

import com.sptp.backend.art_work.repository.ArtWork;
import com.sptp.backend.art_work.repository.ArtWorkRepository;
import com.sptp.backend.chat_room.repository.ChatRoom;
import com.sptp.backend.chat_room.repository.ChatRoomRepository;
import com.sptp.backend.common.exception.CustomException;
import com.sptp.backend.common.exception.ErrorCode;
import com.sptp.backend.member.repository.Member;
import com.sptp.backend.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatRoomServiceTest {

    @InjectMocks
    ChatRoomService chatRoomService;

    @Mock
    ChatRoomRepository chatRoomRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    ArtWorkRepository artWorkRepository;

    @Nested
    class createChatRoomTest {

        long artistId = 1L;
        long memberId = 2L;
        long artWorkId = 4L;
        long chatRoomId = 4L;
        Member artist;
        Member member;
        ArtWork artWork;
        ChatRoom chatRoom;

        @BeforeEach
        void init() {
            artist = Member.builder()
                    .id(artistId)
                    .userId("artist")
                    .password("12345678")
                    .build();

            member = Member.builder()
                    .id(memberId)
                    .userId("member")
                    .password("12345678")
                    .build();

            artWork = ArtWork.builder()
                    .id(artWorkId)
                    .build();

            chatRoom = ChatRoom.builder()
                    .id(chatRoomId)
                    .artist(artist)
                    .member(member)
                    .artWork(artWork)
                    .build();
        }

        @Test
        void successWhenChatRoomIsPresent() {
            //given
            when(chatRoomRepository.findByMemberIdAndArtistIdAndArtWorkId(anyLong(), anyLong(), anyLong()))
                    .thenReturn(Optional.of(chatRoom));

            //when
            //then
            assertThatNoException().isThrownBy(() -> chatRoomService.createChatRoom(memberId, artistId, artWorkId));
        }

        @Test
        void successWhenChatRoomIsNotPresent() {
            //given
            when(chatRoomRepository.findByMemberIdAndArtistIdAndArtWorkId(anyLong(), anyLong(), anyLong()))
                    .thenReturn(Optional.empty());

            when(memberRepository.findById(memberId))
                    .thenReturn(Optional.of(member));

            when(memberRepository.findById(artistId))
                    .thenReturn(Optional.of(artist));

            when(artWorkRepository.findById(artWorkId))
                    .thenReturn(Optional.of(artWork));

            when(chatRoomRepository.save(any(ChatRoom.class)))
                    .thenReturn(chatRoom);

            //when
            //then
            assertThatNoException().isThrownBy(() -> chatRoomService.createChatRoom(memberId, artistId, artWorkId));
        }

        @Test
        void failByNotFoundLoginMember() {
            //given
            when(chatRoomRepository.findByMemberIdAndArtistIdAndArtWorkId(anyLong(), anyLong(), anyLong()))
                    .thenReturn(Optional.empty());

            when(memberRepository.findById(memberId))
                    .thenReturn(Optional.empty());

            //when
            //then
            assertThatThrownBy(() -> chatRoomService.createChatRoom(memberId, artistId, artWorkId))
                    .isInstanceOf(CustomException.class)
                    .message().isEqualTo(ErrorCode.NOT_FOUND_MEMBER.getDetail());
        }

        @Test
        void failByNotFoundArtist() {
            //given
            when(chatRoomRepository.findByMemberIdAndArtistIdAndArtWorkId(anyLong(), anyLong(), anyLong()))
                    .thenReturn(Optional.empty());

            when(memberRepository.findById(memberId))
                    .thenReturn(Optional.of(member));

            when(memberRepository.findById(artistId))
                    .thenReturn(Optional.empty());

            //when
            //then
            assertThatThrownBy(() -> chatRoomService.createChatRoom(memberId, artistId, artWorkId))
                    .isInstanceOf(CustomException.class)
                    .message().isEqualTo(ErrorCode.NOT_FOUND_ARTIST.getDetail());
        }

        @Test
        void failByNotFoundArtWork() {
            //given
            when(chatRoomRepository.findByMemberIdAndArtistIdAndArtWorkId(anyLong(), anyLong(), anyLong()))
                    .thenReturn(Optional.empty());

            when(memberRepository.findById(memberId))
                    .thenReturn(Optional.of(member));

            when(memberRepository.findById(artistId))
                    .thenReturn(Optional.of(artist));

            when(artWorkRepository.findById(artWorkId))
                    .thenReturn(Optional.empty());

            //when
            //then
            assertThatThrownBy(() -> chatRoomService.createChatRoom(memberId, artistId, artWorkId))
                    .isInstanceOf(CustomException.class)
                    .message().isEqualTo(ErrorCode.NOT_FOUND_ARTWORK.getDetail());
        }
    }

}