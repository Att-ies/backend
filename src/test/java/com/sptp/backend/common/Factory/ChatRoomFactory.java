package com.sptp.backend.common.Factory;

import com.sptp.backend.art_work.repository.ArtWork;
import com.sptp.backend.chat_room.repository.ChatRoom;
import com.sptp.backend.member.repository.Member;
import org.springframework.stereotype.Component;

@Component
public class ChatRoomFactory {

    private static Long id = 1L;

    public ChatRoom createChatRoom(Member artist, Member member, ArtWork artWork) {

        return ChatRoom.builder()
                .id(id++)
                .artist(artist)
                .member(member)
                .artWork(artWork)
                .build();
    }
}