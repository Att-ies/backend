package com.sptp.backend.art_work.web;

import com.sptp.backend.art_work.service.ArtWorkService;
import com.sptp.backend.art_work.web.dto.request.ArtWorkBidRequest;
import com.sptp.backend.art_work.web.dto.request.ArtWorkEditRequestDto;
import com.sptp.backend.art_work.web.dto.request.ArtWorkSaveRequestDto;
import com.sptp.backend.art_work.web.dto.response.*;
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
    public ResponseEntity<ArtWorkInfoResponseDto> saveArtWork(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             ArtWorkSaveRequestDto artWorkSaveRequestDto) throws IOException {

        Long savedArtWorkId = artWorkService.saveArtWork(userDetails.getMember().getId(), artWorkSaveRequestDto);

        ArtWorkInfoResponseDto artWorkInfoResponseDto = artWorkService.getArtWork(savedArtWorkId, userDetails.getMember());

        return ResponseEntity.status(HttpStatus.OK).body(artWorkInfoResponseDto);
    }

    // 작품 상세 조회
    @GetMapping("/{artWorkId}")
    public ResponseEntity<ArtWorkInfoResponseDto> getArtWork(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                             @PathVariable("artWorkId") Long artWorkId) {

        ArtWorkInfoResponseDto artWorkInfoResponseDto = artWorkService.getArtWork(artWorkId, userDetails.getMember());

        return ResponseEntity.status(HttpStatus.OK).body(artWorkInfoResponseDto);
    }

    // 내 등록 작품 조회
    @GetMapping("/me")
    public ResponseEntity<List<ArtWorkMyListResponseDto>> getMyArtWorkList(@AuthenticationPrincipal CustomUserDetails userDetails) {

        List<ArtWorkMyListResponseDto> artWorkMyListResponseDto = artWorkService.getMyArtWorkList(userDetails.getMember().getId());

        return ResponseEntity.status(HttpStatus.OK).body(artWorkMyListResponseDto);
    }

    // 응찰하기
    @PutMapping("/{artWorkId}/bidding")
    public ResponseEntity<Void> bid(@AuthenticationPrincipal CustomUserDetails userDetails,
                                    @PathVariable Long artWorkId, @Valid @RequestBody ArtWorkBidRequest artWorkBidRequest) {

        artWorkService.bid(userDetails.getMember().getId(), artWorkId, artWorkBidRequest.getPrice());

        return ResponseEntity.ok().build();
    }

    // 응찰 내역 조회
    @GetMapping("/{artWorkId}/bidding")
    public ResponseEntity<BiddingListResponse> getBiddingList(@PathVariable Long artWorkId) {

        return ResponseEntity.ok(artWorkService.getBiddingList(artWorkId));
    }

    // 나의 경매 조회
    @GetMapping("/bidding/me")
    public ResponseEntity<ArtWorkPurchasedListResponseDto> getMyBidding(@AuthenticationPrincipal CustomUserDetails userDetails) {

        ArtWorkPurchasedListResponseDto artWorkPurchasedListResponseDto = artWorkService.getMyBidding(userDetails.getMember());

        return ResponseEntity.status(HttpStatus.OK).body(artWorkPurchasedListResponseDto);
    }

    // 작품 수정 폼
    @GetMapping("/edit/{artWorkId}")
    public ResponseEntity<ArtWorkEditFormResponse> getEditForm(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                               @PathVariable("artWorkId") Long artWorkId) {

        ArtWorkEditFormResponse artWorkEditFormResponse = artWorkService.getEditForm(userDetails.getMember(), artWorkId);

        return ResponseEntity.status(HttpStatus.OK).body(artWorkEditFormResponse);
    }

    // 작품 수정
    @PostMapping("/edit/{artWorkId}")
    public ResponseEntity<Void> editArtWork(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @PathVariable("artWorkId") Long artWorkId,
                                            ArtWorkEditRequestDto artWorkEditRequestDto) throws IOException {

        artWorkService.editArtWork(userDetails.getMember(), artWorkId, artWorkEditRequestDto);

        return ResponseEntity.ok().build();
    }

}
