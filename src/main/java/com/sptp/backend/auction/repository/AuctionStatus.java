package com.sptp.backend.auction.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AuctionStatus {

    PROCESSING("processing","진행중"),
    NOT_PROCESSING("notProcessing","등록 완료"),
    TERMINATED("terminated","종료");

    private String type;
    private String name;
}
