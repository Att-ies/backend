package com.sptp.backend.auction.service;

import com.sptp.backend.art_work.repository.ArtWork;
import com.sptp.backend.art_work.repository.ArtWorkRepository;
import com.sptp.backend.auction.repository.Auction;
import com.sptp.backend.auction.repository.AuctionRepository;
import com.sptp.backend.auction.repository.AuctionStatus;
import com.sptp.backend.auction.web.dto.request.AuctionSaveRequestDto;
import com.sptp.backend.auction.web.dto.request.AuctionStartRequestDto;
import com.sptp.backend.auction.web.dto.request.AuctionTerminateRequestDto;
import com.sptp.backend.bidding.repository.BiddingRepository;
import com.sptp.backend.common.exception.CustomException;
import com.sptp.backend.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final ArtWorkRepository artWorkRepository;
    private final BiddingRepository biddingRepository;

    @Transactional
    public void saveAuction(AuctionSaveRequestDto dto) {

        if (auctionRepository.existsByTurn(dto.getTurn())) {
            throw new CustomException(ErrorCode.EXIST_AUCTION_TURN);
        }

        Auction auction = Auction.builder()
                .turn(dto.getTurn())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .status(AuctionStatus.SCHEDULED.getType())
                .build();

        auctionRepository.save(auction);
    }

    @Transactional
    public void startAuction(AuctionStartRequestDto dto) {

        Auction auction = auctionRepository.findByTurn(dto.getTurn()).orElseThrow(() -> {
            throw new CustomException(ErrorCode.NOT_FOUND_AUCTION_TURN);
        });

        auction.statusToProcessing();

        artWorkRepository.updateStatusToProcessing(auction.getId());
    }

    @Transactional
    public void terminateAuction(AuctionTerminateRequestDto dto) {

        Auction auction = auctionRepository.findByTurn(dto.getTurn()).orElseThrow(() -> {
            throw new CustomException(ErrorCode.NOT_FOUND_AUCTION_TURN);
        });

        List<ArtWork> artWorks = artWorkRepository.findByAuctionId(auction.getId());

        auction.statusToTerminate();

        updateStatusToTerminated(artWorks);
    }

    private void updateStatusToTerminated(List<ArtWork> artWorks) {

        for (ArtWork artWork : artWorks) {
            if (biddingRepository.existsByArtWorkId(artWork.getId())) {
                artWork.statusToSalesSuccess();
            }else {
                artWork.statusToSalesFailed();
            }
        }
    }
}
