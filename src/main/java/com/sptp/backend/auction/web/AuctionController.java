package com.sptp.backend.auction.web;

import com.sptp.backend.auction.service.AuctionService;
import com.sptp.backend.auction.web.dto.request.AuctionSaveRequestDto;
import com.sptp.backend.auction.web.dto.request.AuctionStartRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
