package com.sptp.backend.exhibition.service;

import com.sptp.backend.art_work.repository.ArtWork;
import com.sptp.backend.art_work.repository.ArtWorkRepository;
import com.sptp.backend.auction.repository.Auction;
import com.sptp.backend.auction.repository.AuctionRepository;
import com.sptp.backend.common.exception.CustomException;
import com.sptp.backend.common.exception.ErrorCode;
import com.sptp.backend.exhibition.web.dto.response.ExhibitionAuctionResponseDto;
import com.sptp.backend.exhibition.web.dto.response.ExhibitionListResponseDto;
import com.sptp.backend.exhibition.web.dto.response.ExhibitionResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExhibitionService {

    private final AuctionRepository auctionRepository;
    private final ArtWorkRepository artWorkRepository;

    @Value("${aws.storage.url}")
    private String storageUrl;

    @Transactional(readOnly = true)
    public List<ExhibitionAuctionResponseDto> getExhibitList() {

        List<ExhibitionAuctionResponseDto> auctionResponseDtoList = new ArrayList<>();

        Auction currentlyProcessingAuction = auctionRepository.findCurrentlyProcessingAuction();
        List<Auction> terminatedAuctionList = auctionRepository.findTerminatedAuction();

        if (!Objects.isNull(currentlyProcessingAuction)) {

            List<ArtWork> artWorkList = artWorkRepository.findByAuctionId(currentlyProcessingAuction.getId());
            auctionResponseDtoList.add(ExhibitionAuctionResponseDto.from(
                    currentlyProcessingAuction,getRandomImage(artWorkList), artWorkList.size()));
        }

        for (Auction terminatedAuction : terminatedAuctionList) {

            List<ArtWork> artWorkList = artWorkRepository.findByAuctionId(terminatedAuction.getId());
            auctionResponseDtoList.add(ExhibitionAuctionResponseDto.from(
                    terminatedAuction, getRandomImage(artWorkList), artWorkList.size()));
        }

        return auctionResponseDtoList;
    }

    private String getRandomImage(List<ArtWork> artWorkList) {

        Random random = new Random();

        if (artWorkList.isEmpty()) {
            return null;
        }

        int value = random.nextInt(artWorkList.size());

        return storageUrl + artWorkList.get(value).getMainImage();
    }

    public List<ExhibitionResponseDto> getExhibitArtWorks(Long auctionId) {

        List<ArtWork> artWorkList = artWorkRepository.findByAuctionIdOrderByCreatedDateDesc(auctionId);

        return artWorkList.stream().map(m ->
                new ExhibitionResponseDto(m.getId(), m.getTitle(), m.getMember().getEducation(), m.getDescription(), m.getGenre(), storageUrl + m.getMainImage(), m.getMember().getId())).collect(Collectors.toList());
    }

    public ExhibitionResponseDto getExhibitArtWork(Long artWorkId) {

        ArtWork artWork = artWorkRepository.findById(artWorkId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ARTWORK));

        return ExhibitionResponseDto.builder()
                .id(artWork.getId())
                .title(artWork.getTitle())
                .education(artWork.getMember().getEducation())
                .description(artWork.getDescription())
                .genre(artWork.getGenre())
                .image(storageUrl + artWork.getMainImage())
                .artistId(artWork.getMember().getId())
                .build();
    }
}
