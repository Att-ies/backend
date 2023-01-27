package com.sptp.backend.auction.repository;

import com.sptp.backend.common.entity.BaseEntity;
import com.sptp.backend.common.exception.CustomException;
import com.sptp.backend.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Auction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auction_id")
    private Long id;

    private Integer turn;
    private LocalDateTime startDate;
    private LocalDateTime endDate;


    private String status;

    public void statusToProcessing() {
        this.status = AuctionStatus.PROCESSING.getType();
    }

    public void statusToTerminate() {
        this.status = AuctionStatus.TERMINATED.getType();
    }

    public boolean isValidPeriod(LocalDateTime currentTime) {
        return (currentTime.equals(startDate) || currentTime.isAfter(startDate))
                && (currentTime.equals(endDate) || currentTime.isBefore(endDate));

    }
}
