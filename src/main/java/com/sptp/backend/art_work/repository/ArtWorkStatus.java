package com.sptp.backend.art_work.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum ArtWorkStatus {

    REGISTERED("registered", "등록됨"),
    PROCESSING("processing","진행중"),
    SALES_SUCCESS("sales_success","낙찰됨"),
    SALES_FAILED("sales_failed","유찰됨");

    private String type;
    private String name;

    public static ArtWorkStatus valueOfType(String type) {

        return Arrays.stream(values())
                .filter(value -> value.type.equals(type))
                .findAny()
                .orElse(null);
    }
}
