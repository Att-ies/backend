package com.sptp.backend.message.repository;

import com.sptp.backend.chat_room.repository.ChatRoom;
import com.sptp.backend.common.config.DBConfig;
import com.sptp.backend.common.config.PropertyConfig;
import com.sptp.backend.common.entity.BaseEntity;
import com.sptp.backend.member.repository.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({DBConfig.class, PropertyConfig.class})
class MessageRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    MessageRepository messageRepository;

    ChatRoom chatRoom;
    int messageCount = 5;

    @BeforeEach
    void init() {
        chatRoom = ChatRoom.builder().build();

        entityManager.persist(chatRoom);

        for (int i = 0; i < messageCount; i++) {
            entityManager.persist(Message.builder()
                    .message("테스트 메시지")
                    .chatRoom(chatRoom)
                    .build());
            ;
        }
    }

    @Test
    void findAllByChatRoomOrderByCreatedDateAsc() {
        //given
        //when
        List<Message> messages = messageRepository.findAllByChatRoomOrderByIdAsc(chatRoom);

        //then
        assertThat(messages).hasSize(messageCount);
        assertThat(messages).isSortedAccordingTo(Comparator.comparing(Message::getId)); // id 오름차순 검증
    }
}