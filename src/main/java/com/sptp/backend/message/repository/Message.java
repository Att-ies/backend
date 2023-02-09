package com.sptp.backend.message.repository;

import com.sptp.backend.chat_room.repository.ChatRoom;
import com.sptp.backend.common.entity.BaseEntity;
import com.sptp.backend.member.repository.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ChatRoom chatRoom;

    private Boolean isRead;
    private String type;

    @Column(length = 600)
    private String content; // type == text -> massage 저장, type == image -> url 저장

    public void read(long memberId) {
        boolean isMyMessage = sender.getId().equals(memberId);

        if (!isMyMessage) {
            isRead = true;
        }
    }
}