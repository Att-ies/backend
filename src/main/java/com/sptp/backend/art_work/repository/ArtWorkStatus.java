package com.sptp.backend.art_work.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum ArtWorkStatus {

    REGISTERED("registered", "경매전"),
    PROCESSING("processing", "입찰중"),
    SALES_SUCCESS("sales_success", "낙찰"),
    SALES_FAILED("sales_failed", "유찰");

    private String type;
    private String name;

    public static ArtWorkStatus valueOfType(String type) {

        return Arrays.stream(values())
                .filter(value -> value.type.equals(type))
                .findAny()
                .orElse(null);
    }
}
