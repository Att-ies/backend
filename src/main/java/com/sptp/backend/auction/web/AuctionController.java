package com.sptp.backend.auction.web;

import com.sptp.backend.art_work.service.ArtWorkService;
import com.sptp.backend.art_work.web.dto.response.ArtWorkTerminatedListResponseDto;
import com.sptp.backend.auction.service.AuctionService;
import com.sptp.backend.auction.web.dto.request.AuctionSaveRequestDto;
import com.sptp.backend.auction.web.dto.request.AuctionStartRequestDto;
import com.sptp.backend.auction.web.dto.request.AuctionTerminateRequestDto;
import com.sptp.backend.auction.web.dto.response.AuctionArtWorkListResponseDto;
import com.sptp.backend.auction.web.dto.response.AuctionListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;
    private final ArtWorkService artWorkService;

    // 경매 생성
    @PostMapping("/auction")
    public ResponseEntity<Void> saveAuction(@RequestBody AuctionSaveRequestDto auctionSaveRequestDto) {

        auctionService.saveAuction(auctionSaveRequestDto);

        return new ResponseEntity(HttpStatus.OK);
    }

    // 경매 시작
    @PatchMapping("/auction")
    public ResponseEntity<Void> startAuction(@RequestBody AuctionStartRequestDto auctionStartRequestDto) {

        auctionService.startAuction(auctionStartRequestDto);

        return new ResponseEntity(HttpStatus.OK);
    }

    // 경매 종료
    @DeleteMapping("/auction")
    public ResponseEntity<Void> terminateAuction(@RequestBody AuctionTerminateRequestDto auctionTerminateRequestDto) {

        auctionService.terminateAuction(auctionTerminateRequestDto);

        return new ResponseEntity(HttpStatus.OK);
    }

    // 현재 경매 작품 조회
    @GetMapping("/auction/art-works")
    public ResponseEntity<AuctionArtWorkListResponseDto> getProcessingAuction() {

        AuctionArtWorkListResponseDto auctionArtWorkListResponseDto = artWorkService.getProcessingArtWorkList();

        return ResponseEntity.status(HttpStatus.OK).body(auctionArtWorkListResponseDto);
    }

    // 경매 일정 조회
    @GetMapping("/auction")
    public ResponseEntity<List<AuctionListResponseDto>> getAuctionList() {

        List<AuctionListResponseDto> auctionListResponseDto = auctionService.getAuctionList();

        return ResponseEntity.status(HttpStatus.OK).body(auctionListResponseDto);
    }

    // 지난 경매 조회
    @GetMapping("/auction/period-over")
    public ResponseEntity<List<AuctionListResponseDto>> getTerminatedAuctionList() {

        List<AuctionListResponseDto> auctionListResponseDto = auctionService.getTerminatedAuctionList();

        return ResponseEntity.status(HttpStatus.OK).body(auctionListResponseDto);
    }

    // 지난 경매 작품 조회
    @GetMapping("/auction/period-over/{auctionId}")
    public ResponseEntity<ArtWorkTerminatedListResponseDto> getTerminatedAuctionArtWorkList(Pageable pageable,
                                                                                            @PathVariable("auctionId") Long auctionId,
                                                                                            @RequestParam(value = "artWorkId", required = false) Long artWorkId) {

        ArtWorkTerminatedListResponseDto terminatedAuctionArtWorkList = artWorkService.getTerminatedAuctionArtWorkList(auctionId, artWorkId, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(terminatedAuctionArtWorkList);
    }
}
