package com.sptp.backend.bidding.repository;

import com.sptp.backend.art_work.repository.ArtWork;
import com.sptp.backend.common.entity.BaseEntity;
import com.sptp.backend.common.exception.CustomException;
import com.sptp.backend.common.exception.ErrorCode;
import com.sptp.backend.member.repository.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Bidding extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bidding_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "art_work_id")
    private ArtWork artWork;
    private Long price;

    public void validateAuctionPeriod() {
        if (!artWork.getAuction().isValidPeriod(getCreatedDate())) {
            throw new CustomException(ErrorCode.NOT_VALID_AUCTION_PERIOD);
        }
    }

    public void raisePrice(long topPrice, long price) {
        if (!isValidPrice(topPrice, price)) {
            throw new CustomException(ErrorCode.NOT_VALID_BID);
        }

        this.price = price;
    }

    private boolean isValidPrice(Long topPrice, Long price) {

        long priceDifference = price - topPrice;

        if (topPrice < 300_000) {
            if (priceDifference >= 20_000) {
                return true;
            }
        } else if (topPrice < 1_000_000) {
            if (priceDifference >= 50_000) {
                return true;
            }
        } else if (topPrice < 3_000_000) {
            if (priceDifference >= 100_000) {
                return true;
            }
        } else if (topPrice < 5_000_000) {
            if (priceDifference >= 200_000) {
                return true;
            }
        } else {
            if (priceDifference >= 500_000) {
                return true;
            }
        }

        return false;
    }

    public void setArtWork(ArtWork artWork) {
        this.artWork = artWork;
        artWork.getBiddingList().add(this);
    }
}
