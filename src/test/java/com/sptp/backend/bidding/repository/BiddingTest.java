package com.sptp.backend.bidding.repository;

import com.sptp.backend.art_work.repository.ArtWork;
import com.sptp.backend.auction.repository.Auction;
import com.sptp.backend.common.exception.CustomException;
import com.sptp.backend.common.exception.ErrorCode;
import com.sptp.backend.member.repository.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.*;

class BiddingTest {

    ArtWork artWork;
    Auction auction;
    Bidding bidding;
    Long startPrice = 100_000L;
    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @BeforeEach
    void init() {
        auction = Auction.builder()
                .id(1L)
                .startDate(LocalDateTime.parse("2023-01-18 00:00:00", format))
                .endDate(LocalDateTime.parse("2023-01-19 00:00:00", format))
                .build();

        artWork = ArtWork.builder()
                .auction(auction)
                .price(startPrice)
                .build();
    }

    @Nested
    class ValidateAuctionPeriodTest {
        @ParameterizedTest
        @ValueSource(strings = {"2023-01-18 00:00:00", "2023-01-19 00:00:00"})
        void success(String localDateTimeStr) {
            //given
            LocalDateTime localDateTime = LocalDateTime.parse(localDateTimeStr, format);

            bidding = Bidding.builder()
                    .artWork(artWork)
                    .member(Member.builder().build())
                    .price(Long.valueOf(startPrice))
                    .createdDate(localDateTime)
                    .build();

            //when

            //then
            assertThatNoException().isThrownBy(() -> bidding.validateAuctionPeriod());
        }

        @ParameterizedTest
        @ValueSource(strings = {"2023-01-17 23:59:59", "2023-01-19 00:00:01"})
        void fail(String localDateTimeStr) {
            //given
            LocalDateTime localDateTime = LocalDateTime.parse(localDateTimeStr, format);

            bidding = Bidding.builder()
                    .artWork(artWork)
                    .member(Member.builder().build())
                    .price(Long.valueOf(startPrice))
                    .createdDate(localDateTime)
                    .build();

            //when
            //then
            assertThatThrownBy(() -> bidding.validateAuctionPeriod())
                    .isInstanceOf(CustomException.class)
                    .message().isEqualTo(ErrorCode.NOT_VALID_AUCTION_PERIOD.getDetail());
        }
    }

    @Nested
    class ValidatePriceTest {

        @ParameterizedTest
        @CsvSource({"299_999,319_999", "300_000,350_000", "999_999,1_049_999", "1_000_000,1_100_000",
                "2_999_999,3_099_999","3_000_000,3_200_000", "4_999_999,5_199_999", "5_000_000,5_500_000"})
        void success(long topPrice, long bid) {
            //given
            bidding = Bidding.builder()
                    .artWork(artWork)
                    .member(Member.builder().build())
                    .build();

            //when
            //then
            assertThatNoException().isThrownBy(() -> bidding.raisePrice(topPrice, bid));
            assertThat(bidding.getPrice()).isEqualTo(bid);
        }

        @ParameterizedTest
        @CsvSource({"299_999,319_998", "300_000,349_999", "999_999,1_049_998", "1_000_000,1_099_999",
                "2_999_999,3_099_998","3_000_000,3_199_999", "4_999_999,5_199_998", "5_000_000,5_499_999"})
        void fail(long topPrice, long bid) {
            //given
            bidding = Bidding.builder()
                    .artWork(artWork)
                    .member(Member.builder().build())
                    .price(bid)
                    .build();

            //when
            //then
            assertThatThrownBy(() -> bidding.raisePrice(topPrice, bid))
                    .isInstanceOf(CustomException.class)
                    .message().isEqualTo(ErrorCode.NOT_VALID_BID.getDetail());
        }
    }
}