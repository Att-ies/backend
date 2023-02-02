package com.sptp.backend.exhibition.web;

import com.sptp.backend.exhibition.service.ExhibitionService;
import com.sptp.backend.exhibition.web.dto.response.ExhibitionListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exhibit")
public class ExhibitionController {

    private final ExhibitionService exhibitionService;

    @GetMapping
    public ResponseEntity<ExhibitionListResponseDto> getExhibitList() {

        ExhibitionListResponseDto exhibitList = exhibitionService.getExhibitList();

        return ResponseEntity.status(HttpStatus.OK).body(exhibitList);
    }
}
