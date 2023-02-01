package com.sptp.backend.chat_room_connection.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomConnectionRepository extends JpaRepository<ChatRoomConnection, Long> {

    boolean existsBySessionId(String sessionId);
    void deleteBySessionId(String sessionId);
    int countByChatRoomId(Long chatRoomId);
}
