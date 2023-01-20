package com.sptp.backend.auction.web;

import com.sptp.backend.auction.service.AuctionService;
import com.sptp.backend.auction.web.dto.request.AuctionSaveRequestDto;
import com.sptp.backend.auction.web.dto.request.AuctionStartRequestDto;
import com.sptp.backend.auction.web.dto.request.AuctionTerminateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;

    @PostMapping("/art-works/auction")
    public ResponseEntity<Void> saveAuction(@RequestBody AuctionSaveRequestDto auctionSaveRequestDto) {

        auctionService.saveAuction(auctionSaveRequestDto);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PatchMapping("/art-works/auction")
    public ResponseEntity<Void> startAuction(@RequestBody AuctionStartRequestDto auctionStartRequestDto) {

        auctionService.startAuction(auctionStartRequestDto);

        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/art-works/auction")
    public ResponseEntity<Void> terminateAuction(@RequestBody AuctionTerminateRequestDto auctionTerminateRequestDto) {

        auctionService.terminateAuction(auctionTerminateRequestDto);

        return new ResponseEntity(HttpStatus.OK);
    }
}
