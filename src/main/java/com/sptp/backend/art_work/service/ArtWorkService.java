package com.sptp.backend.art_work.service;

import com.querydsl.core.Tuple;
import com.sptp.backend.art_work.event.ArtWorkEvent;
import com.sptp.backend.art_work.repository.*;
import com.sptp.backend.art_work.web.dto.request.ArtWorkEditRequestDto;
import com.sptp.backend.art_work.web.dto.request.ArtWorkSaveRequestDto;
import com.sptp.backend.art_work.web.dto.response.*;
import com.sptp.backend.art_work_image.repository.ArtWorkImage;
import com.sptp.backend.art_work_image.repository.ArtWorkImageRepository;
import com.sptp.backend.art_work_keyword.repository.ArtWorkKeyword;
import com.sptp.backend.art_work_keyword.repository.ArtWorkKeywordRepository;
import com.sptp.backend.auction.repository.Auction;
import com.sptp.backend.auction.repository.AuctionRepository;
import com.sptp.backend.auction.repository.AuctionStatus;
import com.sptp.backend.auction.web.dto.response.AuctionArtWorkListResponseDto;
import com.sptp.backend.aws.service.AwsService;
import com.sptp.backend.aws.service.FileManager;
import com.sptp.backend.bidding.repository.Bidding;
import com.sptp.backend.bidding.repository.BiddingRepository;
import com.sptp.backend.bidding.repository.QBidding;
import com.sptp.backend.common.KeywordMap;
import com.sptp.backend.common.NotificationCode;
import com.sptp.backend.common.entity.BaseEntity;
import com.sptp.backend.common.exception.CustomException;
import com.sptp.backend.common.exception.ErrorCode;
import com.sptp.backend.member.repository.Member;
import com.sptp.backend.member.repository.MemberRepository;
import com.sptp.backend.member_preffereed_art_work.repository.MemberPreferredArtWorkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ArtWorkService extends BaseEntity {

    private final ArtWorkRepository artWorkRepository;
    private final ArtWorkKeywordRepository artWorkKeywordRepository;
    private final ArtWorkImageRepository artWorkImageRepository;
    private final MemberRepository memberRepository;
    private final AwsService awsService;
    private final FileManager fileManager;

    private final BiddingRepository biddingRepository;
    private final ApplicationEventPublisher eventPublisher;

    private final AuctionRepository auctionRepository;
    private final MemberPreferredArtWorkRepository memberPreferredArtWorkRepository;

    @Value("${aws.storage.url}")
    private String storageUrl;

    @Transactional
    public Long saveArtWork(Long loginMemberId, ArtWorkSaveRequestDto dto) throws IOException {

        List<Auction> latestScheduledAuction = auctionRepository.findLatestScheduledAuction();
        if (latestScheduledAuction.size() == 0) {
            throw new CustomException(ErrorCode.NOT_FOUND_AUCTION_SCHEDULED);
        }

        Member findMember = getMemberOrThrow(loginMemberId);

        checkExistsImage(dto);

        String guaranteeImageUUID = UUID.randomUUID().toString();
        String guaranteeImageEXT = fileManager.extractExtension(dto.getGuaranteeImage().getOriginalFilename());

        String mainImageUUID = UUID.randomUUID().toString();
        String mainImageEXT = fileManager.extractExtension(dto.getImage()[0].getOriginalFilename());

        ArtWork artWork = ArtWork.builder()
                .member(findMember)
                .title(dto.getTitle())
                .material(dto.getMaterial())
                .price(dto.getPrice())
                .status(dto.getStatus())
                .statusDescription(dto.getStatusDescription())
                .guaranteeImage(guaranteeImageUUID + "." + guaranteeImageEXT)
                .mainImage(mainImageUUID + "." + mainImageEXT)
                .genre(dto.getGenre())
                .artWorkSize(ArtWorkSize.builder().size(dto.getSize()).length(dto.getLength()).width(dto.getWidth()).height(dto.getHeight()).build())
                .frame(dto.isFrame())
                .description(dto.getDescription())
                .productionYear(dto.getProductionYear())
                .auction(latestScheduledAuction.get(0))
                .saleStatus(ArtWorkStatus.REGISTERED.getType())
                .biddingList(new ArrayList<>())
                .build();

        ArtWork savedArtWork = artWorkRepository.save(artWork);
        awsService.uploadImage(dto.getGuaranteeImage(), guaranteeImageUUID);
        awsService.uploadImage(dto.getImage()[0], mainImageUUID);
        saveArtImages(dto.getImage(), artWork);
        saveArtKeywords(dto.getKeywords(), artWork);

        eventPublisher.publishEvent(new ArtWorkEvent(findMember, artWork, null, NotificationCode.SAVE_ARTWORK));

        return savedArtWork.getId();
    }

    public void saveArtImages(MultipartFile[] files, ArtWork artWork) throws IOException {

        if (files[0].isEmpty()) {
            return;
        }

        for (MultipartFile file : files) {

            String imageUUID = UUID.randomUUID().toString();
            String imageEXT = fileManager.extractExtension(file.getOriginalFilename());

            ArtWorkImage artWorkImage = ArtWorkImage.builder()
                    .artWork(artWork)
                    .image(imageUUID + "." + imageEXT)
                    .build();

            artWorkImageRepository.save(artWorkImage);
            awsService.uploadImage(file, imageUUID);
        }
    }

    public void saveArtKeywords(String[] keywords, ArtWork artWork) {

        for (String keyword : keywords) {

            KeywordMap.checkExistsKeyword(keyword);

            ArtWorkKeyword artWorkKeyword = ArtWorkKeyword.builder()
                    .artWork(artWork)
                    .keywordId(KeywordMap.map.get(keyword))
                    .build();

            artWorkKeywordRepository.save(artWorkKeyword);
        }
    }

    public void checkExistsImage(ArtWorkSaveRequestDto dto) {
        if (dto.getGuaranteeImage().isEmpty() || dto.getImage()[0].isEmpty()) {
            throw new CustomException(ErrorCode.SHOULD_EXIST_IMAGE);
        }
    }

    public void bid(Long loginMemberId, Long artWorkId, Long price) {

        ArtWork artWork = getArtWorkOrThrow(artWorkId);
        Member member = getMemberOrThrow(loginMemberId);

        if (artWork.getMember().getId().equals(loginMemberId)) {
            throw new CustomException(ErrorCode.NOT_VALID_REQUEST);
        }

        // 작품에 대한 최초 응찰일 경우
        if (!biddingRepository.existsByArtWorkId(artWork.getId())) {

            // 시작가에 맞춰 응찰할 경우
            if(price.equals(artWork.getPrice())) {

                Bidding firstBidding = Bidding.builder().member(member).artWork(artWork).price(price).build();
                biddingRepository.save(firstBidding);
                validateAuctionPeriod(artWork.getAuction());
                eventPublisher.publishEvent(new ArtWorkEvent(member, artWork, firstBidding, NotificationCode.SUGGEST_BID));
                return;

            } // 시작가보다 높은 가격에 응찰할 경우
            else if (price > artWork.getPrice()) {

                if (!isValidPrice(artWork.getPrice(), price)) {
                    throw new CustomException(ErrorCode.NOT_VALID_BID);
                }

                Bidding firstBidding = Bidding.builder().member(member).artWork(artWork).price(price).build();
                biddingRepository.save(firstBidding);
                validateAuctionPeriod(artWork.getAuction());
                eventPublisher.publishEvent(new ArtWorkEvent(member, artWork, firstBidding, NotificationCode.SUGGEST_BID));
                return;
            }

            throw new CustomException(ErrorCode.NOT_VALID_BID);

        }else{

            Long topPrice = getTopPrice(artWork);
            Bidding bidding = biddingRepository.findByArtWorkAndMember(artWork, member)
                    .orElseGet(() -> saveBidding(artWork, member));

            validateAuctionPeriod(artWork.getAuction());

            bidding.raisePrice(topPrice, price);
            eventPublisher.publishEvent(new ArtWorkEvent(member, artWork, bidding, NotificationCode.SUGGEST_BID));
            eventPublisher.publishEvent(new ArtWorkEvent(member, artWork, null, NotificationCode.STILL_BID));
        }
    }

    private ArtWork getArtWorkOrThrow(Long artWorkId) {
        return artWorkRepository.findById(artWorkId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ARTWORK));
    }

    private Member getMemberOrThrow(Long loginMemberId) {
        return memberRepository.findById(loginMemberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
    }

    private Long getTopPrice(ArtWork artWork) {

        Optional<Bidding> topPriceBiddingOptional = biddingRepository.getFirstByArtWorkOrderByPriceDesc(artWork);

        if (topPriceBiddingOptional.isEmpty()) {
            return Long.valueOf(artWork.getPrice());
        }

        return topPriceBiddingOptional.get().getPrice();
    }

    private Bidding saveBidding(ArtWork artWork, Member member) {
        Bidding bidding = biddingRepository.save(Bidding.builder()
                .artWork(artWork)
                .member(member)
                .build());

        bidding.setArtWork(artWork);

        return bidding;
    }

    @Transactional(readOnly = true)
    public ArtWorkInfoResponseDto getArtWork(Long artWorkId, Member member) {

        ArtWork findArtWork = artWorkRepository.findById(artWorkId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ARTWORK));

        Member findArtist = memberRepository.findById(findArtWork.getMember().getId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ARTIST));

        boolean isPreferred = false;

        if (memberPreferredArtWorkRepository.existsByMemberAndArtWork(member, findArtWork)) {
            isPreferred = true;
        }

        List<ArtWorkImage> artWorkImages = artWorkImageRepository.findByArtWorkId(artWorkId);
        List<ArtWorkKeyword> artWorkKeywords = artWorkKeywordRepository.findByArtWorkId(artWorkId);

        return ArtWorkInfoResponseDto.builder()
                .artist(ArtWorkInfoResponseDto.ArtistDto.from(findArtist, storageUrl))
                .artWork(ArtWorkInfoResponseDto.ArtWorkDto.from(findArtWork, artWorkImages, artWorkKeywords, storageUrl))
                .isPreferred(isPreferred)
                .turn(findArtWork.getAuction().getTurn())
                .endDate(findArtWork.getAuction().getEndDate())
                .build();
    }

    @Transactional(readOnly = true)
    public List<ArtWorkMyListResponseDto> getMyArtWorkList(Long loginMemberId) {

        Member findMember = memberRepository.findById(loginMemberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        List<ArtWork> findArtWorkList = artWorkRepository.findByMemberId(findMember.getId());

        List<ArtWorkMyListResponseDto> artWorkMyListResponseDtoList = new ArrayList<>();

        for (ArtWork artWork : findArtWorkList) {
            ArtWorkMyListResponseDto artWorkMyListResponseDto = classifyArtWork(artWork);
            artWorkMyListResponseDtoList.add(artWorkMyListResponseDto);
        }

        return artWorkMyListResponseDtoList;
    }

    private ArtWorkMyListResponseDto classifyArtWork(ArtWork artWork) {

        Long topPrice = null;

        if (!artWork.getSaleStatus().equals(ArtWorkStatus.REGISTERED.getType())) {

            if (biddingRepository.existsByArtWorkId(artWork.getId())) {
                topPrice = getTopPrice(artWork);
            }
        }

        return ArtWorkMyListResponseDto.builder()
                .id(artWork.getId())
                .title(artWork.getTitle())
                .turn(artWork.getAuction().getTurn())
                .image(storageUrl + artWork.getMainImage())
                .artistName(artWork.getMember().getNickname())
                .auctionStatus(artWork.getSaleStatus())
                .biddingStatus(topPrice)
                .build();
    }

    @Transactional(readOnly = true)
    public BiddingListResponse getBiddingList(Long artWorkId) {

        ArtWork artWork = getArtWorkOrThrow(artWorkId);
        List<Bidding> biddingList = biddingRepository.findAllByArtWorkOrderByPriceDesc(artWork);

        if (biddingList.isEmpty()) {
            return BiddingListResponse.builder()
                    .artWork(BiddingListResponse.ArtWorkDto.of(artWork, null, fileManager.getFullPath(artWork.getMainImage())))
                    .auction(BiddingListResponse.AuctionDto.from(artWork.getAuction()))
                    .biddingList(biddingList.stream().map(BiddingListResponse.BiddingDto::from)
                            .collect(Collectors.toList()))
                    .totalBiddingCount(biddingList.size())
                    .build();
        }

        return BiddingListResponse.builder()
                .artWork(BiddingListResponse.ArtWorkDto.of(artWork, getTopPrice(artWork), fileManager.getFullPath(artWork.getMainImage())))
                .auction(BiddingListResponse.AuctionDto.from(artWork.getAuction()))
                .biddingList(biddingList.stream().map(BiddingListResponse.BiddingDto::from)
                        .collect(Collectors.toList()))
                .totalBiddingCount(biddingList.size())
                .build();
    }

    public AuctionArtWorkListResponseDto getProcessingArtWorkList() {

        Auction auction = auctionRepository.findCurrentlyProcessingAuction();

        if (Objects.isNull(auction)) {
            throw new CustomException(ErrorCode.NOT_FOUND_AUCTION_PROCESSING);
        }

        List<ArtWork> artWorkList = artWorkRepository.findByAuctionIdAndSaleStatus(auction.getId(), ArtWorkStatus.PROCESSING.getType());

        List<AuctionArtWorkListResponseDto.ArtWorkDto> artWorkDtoList = transferToArtWorkDto(artWorkList);

        AuctionArtWorkListResponseDto auctionArtWorkListResponseDto = AuctionArtWorkListResponseDto.builder()
                .turn(auction.getTurn())
                .endDate(auction.getEndDate())
                .artWorkList(artWorkDtoList)
                .build();

        return auctionArtWorkListResponseDto;
    }

    private List<AuctionArtWorkListResponseDto.ArtWorkDto> transferToArtWorkDto(List<ArtWork> artWorkList) {

        List<AuctionArtWorkListResponseDto.ArtWorkDto> artWorkDtoList = new ArrayList<>();

        for (ArtWork artWork : artWorkList) {

            List<Long> priceList = artWork.getBiddingList().stream().map(m -> m.getPrice()).collect(Collectors.toList());
            Long topPrice = artWork.getPrice();
            if (!priceList.isEmpty()) {
                topPrice = Collections.max(priceList);
            }

            AuctionArtWorkListResponseDto.ArtWorkDto artWorkDto = AuctionArtWorkListResponseDto.ArtWorkDto.from(artWork, topPrice, storageUrl);
            artWorkDtoList.add(artWorkDto);
        }

        return artWorkDtoList;
    }

    public List<ArtWorkDeliveryResponse> getDeliveryList(Long auctionId) {

        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_AUCTION_TURN));

        if (!auction.getStatus().equals(AuctionStatus.TERMINATED.getType())) {
            throw new CustomException(ErrorCode.IS_NOT_TERMINATED_AUCTION);
        }

        List<ArtWorkDeliveryResponse> artWorkDeliveryResponseList = new ArrayList<>();
        List<ArtWork> artWorkList = artWorkRepository.findByAuctionIdAndSaleStatus(auctionId, ArtWorkStatus.SALES_SUCCESS.getType());

        for (ArtWork artWork : artWorkList) {

            Optional<Bidding> bidding = biddingRepository.getFirstByArtWorkOrderByPriceDesc(artWork);

            artWorkDeliveryResponseList.add(ArtWorkDeliveryResponse.builder()
                    .artWork(ArtWorkDeliveryResponse.ArtWorkDto.from(artWork, bidding.get().getPrice()))
                    .artist(ArtWorkDeliveryResponse.MemberDto.from(artWork.getMember()))
                    .member(ArtWorkDeliveryResponse.MemberDto.from(bidding.get().getMember()))
                    .build());
        }

        return artWorkDeliveryResponseList;
    }

    public ArtWorkTerminatedListResponseDto getTerminatedAuctionArtWorkList(Long auctionId, Long artWorkId, Pageable pageable) {

        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_AUCTION_TURN));

        if (!auction.getStatus().equals(AuctionStatus.TERMINATED.getType())) {
            throw new CustomException(ErrorCode.IS_NOT_TERMINATED_AUCTION);
        }

        List<ArtWork> results = artWorkRepository.findTerminatedAuctionArtWorkList(auctionId, artWorkId, pageable);
        List<ArtWorkTerminatedListResponseDto.ArtWorkDto> artWorkDtoList = transferToTerminatedArtWorkDto(results);

        boolean hasNext = false;

        // 조회한 결과 개수가 요청한 페이지 사이즈보다 클 경우, next = true
        if (artWorkDtoList.size() > pageable.getPageSize()) {
            hasNext = true;
            artWorkDtoList.remove(pageable.getPageSize());
        }

        ArtWorkTerminatedListResponseDto artWorkTerminatedListResponseDto = ArtWorkTerminatedListResponseDto.builder()
                .nextPage(hasNext)
                .artWorks(artWorkDtoList)
                .build();

        return artWorkTerminatedListResponseDto;
    }

    private List<ArtWorkTerminatedListResponseDto.ArtWorkDto> transferToTerminatedArtWorkDto(List<ArtWork> artWorkList) {

        List<ArtWorkTerminatedListResponseDto.ArtWorkDto> artWorkDtoList = new ArrayList<>();

        for (ArtWork artWork : artWorkList) {

            List<Long> priceList = artWork.getBiddingList().stream().map(m -> m.getPrice()).collect(Collectors.toList());
            Long topPrice = artWork.getPrice();
            if (!priceList.isEmpty()) {
                topPrice = Collections.max(priceList);
            }

            ArtWorkTerminatedListResponseDto.ArtWorkDto artWorkDto = ArtWorkTerminatedListResponseDto.ArtWorkDto.from(artWork, topPrice, artWork.getBiddingList().size(), storageUrl);
            artWorkDtoList.add(artWorkDto);
        }

        return artWorkDtoList;
    }

    public ArtWorkPurchasedListResponseDto getMyBidding(Member member) {

        List<ArtWorkPurchasedListResponseDto.BiddingDto> biddingDtoList = new ArrayList<>();
        List<ArtWorkPurchasedListResponseDto.SuccessfulBiddingDto> successfulBiddingDtoList = new ArrayList<>();

        List<Tuple> list = biddingRepository.findByMemberWithMaxBidding(member);

        for (Tuple tuple : list) {

            ArtWork findArtWork = tuple.get(QBidding.bidding.artWork);
            Long myMaxBiddingPrice = tuple.get(QBidding.bidding.price.max());

            Long topPrice = biddingRepository.getFirstByArtWorkOrderByPriceDesc(findArtWork).get().getPrice();

            // 해당 작품이 판매 완료 상태인 경우
            if (myMaxBiddingPrice.equals(topPrice) && findArtWork.getSaleStatus().equals(ArtWorkStatus.SALES_SUCCESS.getType())) {
                successfulBiddingDtoList.add(ArtWorkPurchasedListResponseDto.SuccessfulBiddingDto.from(findArtWork, topPrice, storageUrl));
            } else {
                biddingDtoList.add(ArtWorkPurchasedListResponseDto.BiddingDto.from(findArtWork, myMaxBiddingPrice, topPrice, storageUrl));
            }
        }

        return ArtWorkPurchasedListResponseDto.builder()
                .successfulBiddingList(successfulBiddingDtoList)
                .biddingList(biddingDtoList)
                .build();
    }

    private boolean isValidPrice(Long topPrice, Long price) {

        long priceDifference = price - topPrice;

        if (topPrice < 300_000) {
            if (priceDifference >= 20_000) {
                return true;
            }
        } else if (topPrice < 1_000_000) {
            if (priceDifference >= 50_000) {
                return true;
            }
        } else if (topPrice < 3_000_000) {
            if (priceDifference >= 100_000) {
                return true;
            }
        } else if (topPrice < 5_000_000) {
            if (priceDifference >= 200_000) {
                return true;
            }
        } else {
            if (priceDifference >= 500_000) {
                return true;
            }
        }

        return false;
    }

    public void validateAuctionPeriod(Auction auction) {

        if (!auction.getStatus().equals(AuctionStatus.PROCESSING.getType())) {
            throw new CustomException(ErrorCode.NOT_VALID_AUCTION_PERIOD);
        }
    }

    public ArtWorkEditFormResponse getEditForm(Member member, Long artworkId) {

        ArtWork findArtWork = artWorkRepository.findById(artworkId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ARTWORK));

        checkEditArtWork(member, findArtWork);

        List<ArtWorkKeyword> artWorkKeywordList = artWorkKeywordRepository.findByArtWorkId(artworkId);
        List<ArtWorkImage> artWorkImageList = artWorkImageRepository.findByArtWorkId(artworkId);

        return ArtWorkEditFormResponse.from(findArtWork, artWorkImageList, artWorkKeywordList, storageUrl);

    }

    public void editArtWork(Member member, Long artworkId, ArtWorkEditRequestDto dto) throws IOException {

        ArtWork findArtWork = artWorkRepository.findById(artworkId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ARTWORK));

        checkEditArtWork(member, findArtWork);

        artWorkImageRepository.deleteByArtWorkId(findArtWork.getId());
        artWorkKeywordRepository.deleteByArtWorkId(findArtWork.getId());

        String mainImageUrl = "";
        String guaranteeImageUrl = UUID.randomUUID().toString() + "." + fileManager.extractExtension(dto.getGuaranteeImage().getOriginalFilename());

        if (dto.getImage()[0].isEmpty() && dto.getPrevImage().isEmpty()) {
            throw new CustomException(ErrorCode.SHOULD_EXIST_IMAGE);
        }

        mainImageUrl = setMainImageUrl(dto);

        findArtWork.updateArtWork(dto, ArtWorkSize.builder()
                        .size(dto.getSize()).width(dto.getWidth())
                        .height(dto.getHeight()).length(dto.getLength())
                        .build()
                , mainImageUrl, guaranteeImageUrl);

        awsService.uploadImage(dto.getGuaranteeImage(), guaranteeImageUrl.substring(0,guaranteeImageUrl.lastIndexOf(".")));
        awsService.uploadImage(dto.getImage()[0], mainImageUrl.substring(0,mainImageUrl.lastIndexOf(".")));
        savePrevImage(dto, findArtWork);
        saveArtImages(dto.getImage(), findArtWork);
        saveArtKeywords(dto.getKeywords(), findArtWork);
    }

    public String setMainImageUrl(ArtWorkEditRequestDto dto) {

        if (dto.getImage()[0].isEmpty() && !dto.getPrevImage().isEmpty()) {
            return fileManager.extractImageDB(dto.getPrevImage().get(0));
        }

        if (!dto.getImage()[0].isEmpty() && dto.getPrevImage().isEmpty()) {
            return UUID.randomUUID().toString() + "." + fileManager.extractExtension(dto.getImage()[0].getOriginalFilename());
        }

        if (!dto.getImage()[0].isEmpty() && !dto.getPrevImage().isEmpty()) {
            return UUID.randomUUID().toString() + "." + fileManager.extractExtension(dto.getImage()[0].getOriginalFilename());
        }

        return "";
    }

    public void savePrevImage(ArtWorkEditRequestDto dto, ArtWork artWork) {

        for (String image : dto.getPrevImage()) {
            artWorkImageRepository.save(ArtWorkImage.builder().artWork(artWork).image(fileManager.extractImageDB(image)).build());
        }
    }

    public void checkEditArtWork(Member member, ArtWork artWork) {

        if (!artWork.getMember().getId().equals(member.getId())) {
            throw new CustomException(ErrorCode.PERMISSION_DENIED);
        }

        if (!artWork.getSaleStatus().equals(ArtWorkStatus.REGISTERED.getType())) {
            throw new CustomException(ErrorCode.SHOULD_BE_REGISTERED);
        }
    }
}
