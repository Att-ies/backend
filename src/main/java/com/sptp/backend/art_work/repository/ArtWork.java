package com.sptp.backend.art_work.repository;

import com.sptp.backend.auction.repository.Auction;
import com.sptp.backend.auction.repository.AuctionStatus;
import com.sptp.backend.bidding.repository.Bidding;
import com.sptp.backend.common.entity.BaseEntity;
import com.sptp.backend.member.repository.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "artWork")
    private List<Bidding> biddingList = new ArrayList<>();

    private String title;

    private String material;

    private Integer productionYear;

    private Long price;

    private String status;

    private String statusDescription;

    private String guaranteeImage;

    private String mainImage;

    private boolean frame;

    private String genre;

    private String description;

    @Embedded
    private ArtWorkSize artWorkSize;

    private String saleStatus;

    public void statusToProcessing() {
        this.saleStatus= ArtWorkStatus.PROCESSING.getType();
    }

    public void statusToSalesSuccess() {
        this.saleStatus = ArtWorkStatus.SALES_SUCCESS.getType();
    }

    public void statusToSalesFailed() {
        this.saleStatus = ArtWorkStatus.SALES_FAILED.getType();
    }
}
