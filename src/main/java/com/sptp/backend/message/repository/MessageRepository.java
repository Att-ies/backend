package com.sptp.backend.message.repository;

import com.sptp.backend.chat_room.repository.ChatRoom;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @EntityGraph(attributePaths = {"sender"})
    List<Message> findAllByChatRoomOrderByIdAsc(ChatRoom chatRom);
}
