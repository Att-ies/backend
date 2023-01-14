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
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id")
    private Member artist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collector_id")
    private Member collector;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "art_work_id")
    private ArtWork artWork;
}
