package com.sptp.backend.art_work.service;

import com.sptp.backend.art_work.repository.ArtWork;
import com.sptp.backend.art_work.repository.ArtWorkRepository;
import com.sptp.backend.auction.repository.Auction;
import com.sptp.backend.bidding.repository.Bidding;
import com.sptp.backend.bidding.repository.BiddingRepository;
import com.sptp.backend.common.exception.CustomException;
import com.sptp.backend.common.exception.ErrorCode;
import com.sptp.backend.member.repository.Member;
import com.sptp.backend.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArtWorkServiceTest {

    @InjectMocks
    ArtWorkService artWorkService;

    @Mock
    ArtWorkRepository artWorkRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    BiddingRepository biddingRepository;

    @BeforeEach
    void setUp() {

    }

    @Nested
    class bidTest {

        long memberId = 1L;
        long artWorkId = 2L;
        long auctionId = 3L;
        long biddingId = 3L;
        Member member;
        Auction auction;
        ArtWork artWork;
        Bidding bidding;
        Integer startPrice = 100_000;
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDate = LocalDateTime.parse("2023-01-18 00:00:00", format);

        @BeforeEach
        void init() {
            member = Member.builder()
                    .id(memberId)
                    .build();

            auction = Auction.builder()
                    .id(auctionId)
                    .startDate(startDate)
                    .endDate(LocalDateTime.parse("2023-01-19 00:00:00", format))
                    .build();

            artWork = ArtWork.builder()
                    .id(artWorkId)
                    .auction(auction)
                    .price(startPrice)
                    .build();

            bidding = Bidding.builder()
                    .id(biddingId)
                    .artWork(artWork)
                    .member(member)
                    .price(Long.valueOf(startPrice))
                    .createdDate(startDate)
                    .build();
        }

//        @ParameterizedTest(name = )
//        void bid() {
//            //given
//            when(memberRepository.findById(memberId))
//                    .thenReturn(Optional.of(member));
//
//            when(artWorkRepository.findById(artWorkId))
//                    .thenReturn(Optional.of(artWork));
//
//            Long biddingId = 4L;
//            Bidding.builder()
//                            .id(biddingId)
//                                    .artWork(artWork)
//                                            .price()
//            when(biddingRepository.save(any(Bidding.class)))
//                    .thenReturn()
//
//
//            //when
//
//            //then
//        }

        @Test
        void failByNotFoundArtWork() {
            //given
            //when
            //then
            assertThatThrownBy(() -> artWorkService.bid(memberId, artWorkId, Long.valueOf(startPrice)))
                    .isInstanceOf(CustomException.class)
                    .message().isEqualTo(ErrorCode.NOT_FOUND_ARTWORK.getDetail());
        }

        @Test
        void failByNotFoundLoginMember() {
            //given
            when(artWorkRepository.findById(anyLong()))
                    .thenReturn(Optional.of(artWork));

            //when
            //then
            assertThatThrownBy(() -> artWorkService.bid(memberId, artWorkId, Long.valueOf(startPrice)))
                    .isInstanceOf(CustomException.class)
                    .message().isEqualTo(ErrorCode.NOT_FOUND_MEMBER.getDetail());
        }

        @ParameterizedTest
        @ValueSource(strings = {"2023-01-17 23:59:59", "2023-01-19 00:00:01"})
        void failByNotValidPeriod(String localDateTimeStr) {
            //given
            LocalDateTime localDateTime = LocalDateTime.parse(localDateTimeStr, format);

            bidding = Bidding.builder()
                    .artWork(artWork)
                    .createdDate(localDateTime)
                    .build();

            when(artWorkRepository.findById(anyLong()))
                    .thenReturn(Optional.of(artWork));

            when(memberRepository.findById(anyLong()))
                    .thenReturn(Optional.of(member));

            when(biddingRepository.save(any(Bidding.class)))
                    .thenReturn(bidding);

            //when
            //then
            assertThatThrownBy(() -> artWorkService.bid(memberId, artWorkId, Long.valueOf(startPrice)))
                    .isInstanceOf(CustomException.class)
                    .message().isEqualTo(ErrorCode.NOT_VALID_AUCTION_PERIOD.getDetail());
        }

        @Test
        void failByNotValidPriceWhenBiddingIsCreated() {
            //given
            int topPrice = 299_999;
            long bid = 319_998;

            artWork = ArtWork.builder()
                    .id(artWorkId)
                    .auction(auction)
                    .price(topPrice)
                    .build();

            bidding = Bidding.builder()
                    .artWork(artWork)
                    .member(Member.builder().build())
                    .createdDate(startDate)
                    .price(bid)
                    .build();

            when(artWorkRepository.findById(anyLong()))
                    .thenReturn(Optional.of(artWork));

            when(memberRepository.findById(anyLong()))
                    .thenReturn(Optional.of(member));

            when(biddingRepository.save(any(Bidding.class)))
                    .thenReturn(bidding);

            when(biddingRepository.getFirstByArtWorkOrderByPriceDesc(any(ArtWork.class)))
                    .thenReturn(Optional.of(bidding));

            //when
            //then
            assertThatThrownBy(() -> artWorkService.bid(memberId, artWorkId, Long.valueOf(startPrice)))
                    .isInstanceOf(CustomException.class)
                    .message().isEqualTo(ErrorCode.NOT_VALID_BID.getDetail());
        }

        @Test
        void failByNotValidPriceWhenBiddingIsNotCreated() {
            //given
            int topPrice = 299_999;
            long bid = 319_998;

            artWork = ArtWork.builder()
                    .id(artWorkId)
                    .auction(auction)
                    .price(topPrice)
                    .build();

            bidding = Bidding.builder()
                    .artWork(artWork)
                    .member(Member.builder().build())
                    .createdDate(startDate)
                    .price(bid)
                    .build();

            when(artWorkRepository.findById(anyLong()))
                    .thenReturn(Optional.of(artWork));

            when(memberRepository.findById(anyLong()))
                    .thenReturn(Optional.of(member));

            when(biddingRepository.findByArtWorkAndMember(any(ArtWork.class), any(Member.class)))
                    .thenReturn(Optional.of(bidding));

            when(biddingRepository.save(any(Bidding.class)))
                    .thenReturn(bidding);

            //when
            //then
            assertThatThrownBy(() -> artWorkService.bid(memberId, artWorkId, Long.valueOf(startPrice)))
                    .isInstanceOf(CustomException.class)
                    .message().isEqualTo(ErrorCode.NOT_VALID_BID.getDetail());
        }
    }
}