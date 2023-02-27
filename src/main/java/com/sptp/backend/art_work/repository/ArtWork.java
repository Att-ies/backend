package com.sptp.backend.art_work.repository;

import com.sptp.backend.art_work.web.dto.request.ArtWorkEditRequestDto;
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

    @Builder.Default
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

    @Column(length = 3000)
    private String description;

    @Embedded
    private ArtWorkSize artWorkSize;

    private String saleStatus;

    @Builder.Default
    private int likeCount = 0;

    public void statusToProcessing() {
        this.saleStatus = ArtWorkStatus.PROCESSING.getType();
    }

    public void statusToSalesSuccess() {
        this.saleStatus = ArtWorkStatus.SALES_SUCCESS.getType();
    }

    public void statusToSalesFailed() {
        this.saleStatus = ArtWorkStatus.SALES_FAILED.getType();
    }

    public void plusLikeCount() {
        this.likeCount++;
    }

    public void minusLikeCount() {
        this.likeCount--;
    }

    public void updateArtWork(ArtWorkEditRequestDto dto, ArtWorkSize artWorkSize, String mainImageUrl, String guaranteeImageUrl) {

        this.title = dto.getTitle();
        this.productionYear = dto.getProductionYear();
        this.material = dto.getMaterial();
        this.status = dto.getStatus();
        this.statusDescription = dto.getStatusDescription();
        this.artWorkSize = artWorkSize;
        this.frame = dto.isFrame();
        this.genre = dto.getGenre();
        this.price = dto.getPrice();
        this.description = dto.getDescription();
        this.mainImage = mainImageUrl;
        this.guaranteeImage = guaranteeImageUrl;
    }

    public void updateMainImage(String imageUrl) {

        this.mainImage = imageUrl;
    }
}
