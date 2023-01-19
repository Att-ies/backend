package com.sptp.backend.auction.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AuctionStatus {

    SCHEDULED("scheduled","예정"),
    PROCESSING("processing","진행중"),
    TERMINATED("terminated","종료");

    private String type;
    private String name;
}
