package com.sptp.backend.auction.service;

import com.sptp.backend.art_work.repository.ArtWork;
import com.sptp.backend.art_work.repository.ArtWorkRepository;
import com.sptp.backend.auction.event.AuctionEvent;
import com.sptp.backend.auction.repository.Auction;
import com.sptp.backend.auction.repository.AuctionRepository;
import com.sptp.backend.auction.repository.AuctionStatus;
import com.sptp.backend.auction.web.dto.request.AuctionSaveRequestDto;
import com.sptp.backend.auction.web.dto.request.AuctionStartRequestDto;
import com.sptp.backend.auction.web.dto.response.AuctionListResponseDto;
import com.sptp.backend.common.NotificationCode;
import com.sptp.backend.bidding.repository.BiddingRepository;
import com.sptp.backend.common.exception.CustomException;
import com.sptp.backend.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final ArtWorkRepository artWorkRepository;
    private final BiddingRepository biddingRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void saveAuction(AuctionSaveRequestDto dto) {

        if (auctionRepository.existsByTurn(dto.getTurn())) {
            throw new CustomException(ErrorCode.EXIST_AUCTION_TURN);
        }

        if (LocalDateTime.now().isAfter(dto.getStartDate()) || LocalDateTime.now().isAfter(dto.getEndDate())) {
            throw new CustomException(ErrorCode.NOT_VALID_PERIOD);
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
    public void startAuction(Integer turn) {

        Auction auction = auctionRepository.findByTurn(turn).orElseThrow(() -> {
            throw new CustomException(ErrorCode.NOT_FOUND_AUCTION_TURN);
        });

        auction.statusToProcessing();

        artWorkRepository.updateStatusToProcessing(auction.getId());

        List<ArtWork> artWorks = artWorkRepository.findByAuctionId(auction.getId());
        for(ArtWork artWork : artWorks) {
            eventPublisher.publishEvent(new AuctionEvent(artWork, NotificationCode.SAVE_AUCTION));
            eventPublisher.publishEvent(new AuctionEvent(artWork, NotificationCode.SAVE_DISPLAY));
        }
    }

    @Transactional
    public void terminateAuction(Integer turn) {

        Auction auction = auctionRepository.findByTurn(turn).orElseThrow(() -> {
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
                eventPublisher.publishEvent(new AuctionEvent(artWork, NotificationCode.SUCCESSFUL_BID));
            }else {
                artWork.statusToSalesFailed();
                eventPublisher.publishEvent(new AuctionEvent(artWork, NotificationCode.FAILED_BID));
            }
        }
    }

    public List<AuctionListResponseDto> getAuctionList() {

        List<Auction> auctionList = new ArrayList<>();
        Auction currentlyProcessingAuction = auctionRepository.findCurrentlyProcessingAuction();
        List<Auction> scheduledAuction = auctionRepository.findScheduledAuction();
        List<AuctionListResponseDto> auctionListResponseDto = new ArrayList<>();

        auctionList.add(currentlyProcessingAuction);
        for (Auction auction : scheduledAuction) {
            auctionList.add(auction);
        }

        for (Auction auction : auctionList) {

            Long artWorkCount = artWorkRepository.countByAuctionId(auction.getId());
            auctionListResponseDto.add(AuctionListResponseDto.builder()
                    .id(auction.getId()).turn(auction.getTurn())
                    .startDate(auction.getStartDate()).endDate(auction.getEndDate())
                    .status(auction.getStatus()).artWorkCount(artWorkCount).build());
        }

        return auctionListResponseDto;
    }

    public List<AuctionListResponseDto> getTerminatedAuctionList() {

        List<Auction> terminatedAuctionList = auctionRepository.findTerminatedAuction();
        List<AuctionListResponseDto> auctionListResponseDto = new ArrayList<>();

        for (Auction auction : terminatedAuctionList) {

            Long artWorkCount = artWorkRepository.countByAuctionId(auction.getId());
            auctionListResponseDto.add(AuctionListResponseDto.builder()
                    .id(auction.getId()).turn(auction.getTurn())
                    .startDate(auction.getStartDate()).endDate(auction.getEndDate())
                    .status(auction.getStatus()).artWorkCount(artWorkCount).build());
        }

        return auctionListResponseDto;
    }
}
