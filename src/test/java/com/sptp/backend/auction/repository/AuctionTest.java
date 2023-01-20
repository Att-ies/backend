package com.sptp.backend.auction.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.*;

class AuctionTest {

    @Nested
    class IsValidPeriodTest {

        Auction auction;
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        @BeforeEach
        void setUp() {
            auction = Auction.builder()
                    .id(1L)
                    .startDate(LocalDateTime.parse("2023-01-18 00:00:00", format))
                    .endDate(LocalDateTime.parse("2023-01-19 00:00:00", format))
                    .build();
        }

        @ParameterizedTest
        @ValueSource(strings = {"2023-01-18 00:00:00", "2023-01-19 00:00:00"})
        void success(String localDateTimeStr) {
            //given
            LocalDateTime localDateTime = LocalDateTime.parse(localDateTimeStr, format);

            //when
            //then
            assertThat(auction.isValidPeriod(localDateTime)).isTrue();
        }

        @ParameterizedTest
        @ValueSource(strings = {"2023-01-17 23:59:59", "2023-01-19 00:00:01"})
        void fail(String localDateTimeStr) {
            //given
            LocalDateTime localDateTime = LocalDateTime.parse(localDateTimeStr, format);

            //when
            //then
            assertThat(auction.isValidPeriod(localDateTime)).isFalse();
        }
    }
}