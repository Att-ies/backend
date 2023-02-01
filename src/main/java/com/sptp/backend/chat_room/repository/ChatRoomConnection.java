package com.sptp.backend.chat_room.repository;

import com.sptp.backend.art_work.repository.ArtWork;
import com.sptp.backend.common.entity.BaseEntity;
import com.sptp.backend.member.repository.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(uniqueConstraints = {@UniqueConstraint(
        name = "CHAT_ROOM_SESSION_UNIQUE",
        columnNames = {"sessionId"})})
public class ChatRoomConnection extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_connection_id")
    private Long id;

    private String sessionId;
    private Long chatRoomId;
    private Long memberId;
}
