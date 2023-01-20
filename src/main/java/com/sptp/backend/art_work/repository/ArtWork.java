package com.sptp.backend.art_work.repository;

import com.sptp.backend.auction.repository.Auction;
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
public class ArtWork extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "art_work_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Auction auction;

    private String title;

    private String material;

    private Integer productionYear;

    private Integer price; // TODO artWork 타입 Interger -> Long으로 변환 필요

    private String status;

    private String statusDescription;

    private String guaranteeImage;

    private String mainImage;

    private boolean frame;

    private String genre;

    private String description;

    @Embedded
    private ArtWorkSize artWorkSize;

    private String sale_status;
}
