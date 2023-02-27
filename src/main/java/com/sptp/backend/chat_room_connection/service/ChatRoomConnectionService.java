package com.sptp.backend.chat_room_connection.service;

import com.sptp.backend.chat_room_connection.repository.ChatRoomConnection;
import com.sptp.backend.chat_room_connection.repository.ChatRoomConnectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatRoomConnectionService {

    private final ChatRoomConnectionRepository chatRoomConnectionRepository;

    public void connect(String sessionId, Long chatRoomId, Long memberId) {

        if (chatRoomConnectionRepository.existsBySessionId(sessionId)) {
            return;
        }

        ChatRoomConnection chatRoomConnection = ChatRoomConnection.builder()
                .sessionId(sessionId)
                .chatRoomId(chatRoomId)
                .memberId(memberId)
                .build();

        chatRoomConnectionRepository.save(chatRoomConnection);
    }

    public void disconnect(String sessionId) {

        if (chatRoomConnectionRepository.existsBySessionId(sessionId)) {
            chatRoomConnectionRepository.deleteBySessionId(sessionId);
        }
    }
}
