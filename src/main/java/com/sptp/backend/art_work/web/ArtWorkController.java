package com.sptp.backend.art_work.web;

import com.sptp.backend.art_work.service.ArtWorkService;
import com.sptp.backend.art_work.web.dto.request.ArtWorkBidRequest;
import com.sptp.backend.art_work.web.dto.request.ArtWorkSaveRequestDto;
import com.sptp.backend.art_work.web.dto.response.ArtWorkInfoResponseDto;
import com.sptp.backend.art_work.web.dto.response.ArtWorkMyListResponseDto;
import com.sptp.backend.jwt.service.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/art-works")
public class ArtWorkController {

    private final ArtWorkService artWorkService;

    // 작품 등록
    @PostMapping
    public ResponseEntity<Void> saveArtWork(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             ArtWorkSaveRequestDto artWorkSaveRequestDto) throws IOException {

        artWorkService.saveArtWork(userDetails.getMember().getId(), artWorkSaveRequestDto);

        return new ResponseEntity(HttpStatus.OK);
    }

    // 응찰하기
    @PutMapping("/{artWorkId}/bidding")
    public ResponseEntity<Void> bid(@AuthenticationPrincipal CustomUserDetails userDetails,
                                    @PathVariable Long artWorkId, @Valid @RequestBody ArtWorkBidRequest artWorkBidRequest) {

        artWorkService.bid(userDetails.getMember().getId(), artWorkId, artWorkBidRequest.getPrice());

        return ResponseEntity.ok().build();
    }

    // 작품 상세 조회
    @GetMapping("/art-works/{artWorkId}")
    public ResponseEntity<ArtWorkInfoResponseDto> getArtWork(@PathVariable("artWorkId") Long artWorkId) {

        ArtWorkInfoResponseDto artWorkInfoResponseDto = artWorkService.getArtWork(artWorkId);

        return ResponseEntity.status(HttpStatus.OK).body(artWorkInfoResponseDto);
    }

    // 내 등록 작품 조회
    @GetMapping("/art-works/me")
    public ResponseEntity<List<ArtWorkMyListResponseDto>> getMyArtWorkList(@AuthenticationPrincipal CustomUserDetails userDetails) {

        List<ArtWorkMyListResponseDto> artWorkMyListResponseDto = artWorkService.getMyArtWorkList(userDetails.getMember().getId());

        return ResponseEntity.status(HttpStatus.OK).body(artWorkMyListResponseDto);
    }
}
