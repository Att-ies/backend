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
        name = "CHAT_ROOM_UNIQUE",
        columnNames = {"artist_id", "member_id", "art_work_id"})})
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id")
    private Member artist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "art_work_id")
    private ArtWork artWork;

    public Member getOtherMember(Long memberId) {
        if (member.getId().equals(memberId)) {
            return artist;
        }

        return member;
    }
}
