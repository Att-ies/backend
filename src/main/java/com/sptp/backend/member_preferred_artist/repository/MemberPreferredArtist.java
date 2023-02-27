package com.sptp.backend.member_preferred_artist.repository;

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
@Table(name="MEMBER_PREFERRED_ARTIST", uniqueConstraints = {@UniqueConstraint(
        name = "MEMBER_ARTIST_UNIQUE",
        columnNames = {"MEMBER_ID", "ARTIST_ID"} )})
public class MemberPreferredArtist  extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_preferred_artist_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member artist;
}