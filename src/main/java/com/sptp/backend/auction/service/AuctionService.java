package com.sptp.backend.auction.service;

import com.sptp.backend.auction.repository.Auction;
import com.sptp.backend.auction.repository.AuctionRepository;
import com.sptp.backend.auction.repository.AuctionStatus;
import com.sptp.backend.auction.web.dto.request.AuctionSaveRequestDto;
import com.sptp.backend.auction.web.dto.request.AuctionStartRequestDto;
import com.sptp.backend.common.exception.CustomException;
import com.sptp.backend.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;

    @Transactional
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

    @Transactional
    public void startAuction(AuctionStartRequestDto dto) {

        Auction auction = auctionRepository.findByTurn(dto.getTurn()).orElseThrow(() -> {
            throw new CustomException(ErrorCode.NOT_FOUND_AUCTION_TURN);
        });

        auction.statusToProcessing();
    }
}
