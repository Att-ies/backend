package com.sptp.backend.message.repository;

import com.sptp.backend.chat_room.repository.ChatRoom;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @EntityGraph(attributePaths = {"sender"})
    List<Message> findAllByChatRoomOrderByIdAsc(ChatRoom chatRom);

    Optional<Message> findFirstByChatRoomOrderByIdDesc(ChatRoom chatRoom);

    Integer countByChatRoomAndIsReadIsFalse(ChatRoom chatRoom);
}
