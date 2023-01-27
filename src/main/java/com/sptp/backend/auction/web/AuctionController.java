package com.sptp.backend.auction.web;

import com.sptp.backend.art_work.service.ArtWorkService;
import com.sptp.backend.auction.service.AuctionService;
import com.sptp.backend.auction.web.dto.request.AuctionSaveRequestDto;
import com.sptp.backend.auction.web.dto.request.AuctionStartRequestDto;
import com.sptp.backend.auction.web.dto.request.AuctionTerminateRequestDto;
import com.sptp.backend.auction.web.dto.response.AuctionArtWorkListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;
    private final ArtWorkService artWorkService;

    // 경매 생성
    @PostMapping("/art-works/auction")
    public ResponseEntity<Void> saveAuction(@RequestBody AuctionSaveRequestDto auctionSaveRequestDto) {

        auctionService.saveAuction(auctionSaveRequestDto);

        return new ResponseEntity(HttpStatus.OK);
    }

    // 경매 시작
    @PatchMapping("/art-works/auction")
    public ResponseEntity<Void> startAuction(@RequestBody AuctionStartRequestDto auctionStartRequestDto) {

        auctionService.startAuction(auctionStartRequestDto);

        return new ResponseEntity(HttpStatus.OK);
    }

    // 경매 종료
    @DeleteMapping("/art-works/auction")
    public ResponseEntity<Void> terminateAuction(@RequestBody AuctionTerminateRequestDto auctionTerminateRequestDto) {

        auctionService.terminateAuction(auctionTerminateRequestDto);

        return new ResponseEntity(HttpStatus.OK);
    }

    // 현재 경매 작품 조회
    @GetMapping("/art-works/auction")
    public ResponseEntity<AuctionArtWorkListResponseDto> getProcessingAuction() {

        AuctionArtWorkListResponseDto auctionArtWorkListResponseDto = artWorkService.getProcessingArtWorkList();

        return ResponseEntity.status(HttpStatus.OK).body(auctionArtWorkListResponseDto);
    }
}
