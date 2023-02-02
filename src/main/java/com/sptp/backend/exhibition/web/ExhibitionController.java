package com.sptp.backend.exhibition.web;

import com.sptp.backend.exhibition.service.ExhibitionService;
import com.sptp.backend.exhibition.web.dto.response.ExhibitionListResponseDto;
import com.sptp.backend.exhibition.web.dto.response.ExhibitionResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping("/{auctionId}")
    public ResponseEntity<List<ExhibitionResponseDto>> getExhibitArtWorks(@PathVariable("auctionId") Long auctionId) {

        List<ExhibitionResponseDto> exhibitArtWorks = exhibitionService.getExhibitArtWorks(auctionId);

        return ResponseEntity.status(HttpStatus.OK).body(exhibitArtWorks);
    }

    @GetMapping("/art-works/{artWorkId}")
    public ResponseEntity<ExhibitionResponseDto> getExhibitArtWork(@PathVariable("artWorkId") Long artWorkId) {

        ExhibitionResponseDto exhibitArtWork = exhibitionService.getExhibitArtWork(artWorkId);

        return ResponseEntity.status(HttpStatus.OK).body(exhibitArtWork);
    }
}
