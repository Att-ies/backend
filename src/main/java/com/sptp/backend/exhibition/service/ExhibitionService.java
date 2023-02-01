package com.sptp.backend.exhibition.service;

import com.sptp.backend.art_work.repository.ArtWorkRepository;
import com.sptp.backend.auction.repository.Auction;
import com.sptp.backend.auction.repository.AuctionRepository;
import com.sptp.backend.exhibition.web.dto.response.ExhibitionListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ExhibitionService {

    private final AuctionRepository auctionRepository;
    private final ArtWorkRepository artWorkRepository;

    @Transactional(readOnly = true)
    public ExhibitionListResponseDto getExhibitList() {

        List<ExhibitionListResponseDto.ProcessingAuctionDto> processingAuctionDto = new ArrayList<>();
        List<ExhibitionListResponseDto.TerminatedAuctionDto> terminatedAuctionDto = new ArrayList<>();

        Auction currentlyProcessingAuction = auctionRepository.findCurrentlyProcessingAuction();
        List<Auction> terminatedAuctionList = auctionRepository.findTerminatedAuction();

        if (!Objects.isNull(currentlyProcessingAuction)) {

            Long artWorkCount = artWorkRepository.countByAuctionId(currentlyProcessingAuction.getId());
            processingAuctionDto.add(ExhibitionListResponseDto.ProcessingAuctionDto.from(currentlyProcessingAuction, artWorkCount));
        }

        for (Auction terminatedAuction : terminatedAuctionList) {

            Long artWorkCount = artWorkRepository.countByAuctionId(terminatedAuction.getId());
            terminatedAuctionDto.add(ExhibitionListResponseDto.TerminatedAuctionDto.from(terminatedAuction, artWorkCount));
        }

        return ExhibitionListResponseDto.builder()
                .processingAuction(processingAuctionDto)
                .terminatedAuction(terminatedAuctionDto)
                .build();
    }
}
