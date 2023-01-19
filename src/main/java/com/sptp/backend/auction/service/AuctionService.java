package com.sptp.backend.auction.service;

import com.sptp.backend.auction.repository.Auction;
import com.sptp.backend.auction.repository.AuctionRepository;
import com.sptp.backend.auction.repository.AuctionStatus;
import com.sptp.backend.auction.web.dto.request.AuctionSaveRequestDto;
import com.sptp.backend.common.exception.CustomException;
import com.sptp.backend.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;

    public void saveAuction(AuctionSaveRequestDto dto) {

        System.out.println("dto.getTurn() = " + dto.getTurn());
        System.out.println("dto.getStartDate() = " + dto.getStartDate());
        System.out.println("dto.getEndDate() = " + dto.getEndDate());

        if (auctionRepository.existsByTurn(dto.getTurn())) {
            throw new CustomException(ErrorCode.EXIST_AUCTION_TURN);
        }

        Auction auction = Auction.builder()
                .turn(dto.getTurn())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .status(AuctionStatus.NOT_PROCESSING.getType())
                .build();

        auctionRepository.save(auction);
    }
}
