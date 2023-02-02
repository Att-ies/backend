package com.sptp.backend.exhibition.service;

import com.sptp.backend.art_work.repository.ArtWork;
import com.sptp.backend.art_work.repository.ArtWorkRepository;
import com.sptp.backend.auction.repository.Auction;
import com.sptp.backend.auction.repository.AuctionRepository;
import com.sptp.backend.exhibition.web.dto.response.ExhibitionListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ExhibitionService {

    private final AuctionRepository auctionRepository;
    private final ArtWorkRepository artWorkRepository;

    @Value("${aws.storage.url}")
    private String storageUrl;

    @Transactional(readOnly = true)
    public ExhibitionListResponseDto getExhibitList() {

        List<ExhibitionListResponseDto.ProcessingAuctionDto> processingAuctionDto = new ArrayList<>();
        List<ExhibitionListResponseDto.TerminatedAuctionDto> terminatedAuctionDto = new ArrayList<>();

        Auction currentlyProcessingAuction = auctionRepository.findCurrentlyProcessingAuction();
        List<Auction> terminatedAuctionList = auctionRepository.findTerminatedAuction();

        if (!Objects.isNull(currentlyProcessingAuction)) {
            List<ArtWork> artWorkList = artWorkRepository.findByAuctionId(currentlyProcessingAuction.getId());
            processingAuctionDto.add(ExhibitionListResponseDto.ProcessingAuctionDto.from(
                    currentlyProcessingAuction, getRandomImage(artWorkList), artWorkList.size()));
        }

        for (Auction terminatedAuction : terminatedAuctionList) {

            List<ArtWork> artWorkList = artWorkRepository.findByAuctionId(terminatedAuction.getId());
            terminatedAuctionDto.add(ExhibitionListResponseDto.TerminatedAuctionDto.from(
                    terminatedAuction, getRandomImage(artWorkList), artWorkList.size()));
        }

        return ExhibitionListResponseDto.builder()
                .processingAuction(processingAuctionDto)
                .terminatedAuction(terminatedAuctionDto)
                .build();
    }

    private String getRandomImage(List<ArtWork> artWorkList) {

        Random random = new Random();

        if (artWorkList.isEmpty()) {
            return null;
        }

        int value = random.nextInt(artWorkList.size());

        return storageUrl + artWorkList.get(value).getMainImage();
    }
}
