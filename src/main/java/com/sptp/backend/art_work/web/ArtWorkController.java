package com.sptp.backend.art_work.web;

import com.sptp.backend.art_work.service.ArtWorkService;
import com.sptp.backend.art_work.web.dto.request.ArtWorkBidRequest;
import com.sptp.backend.art_work.web.dto.request.ArtWorkSaveRequestDto;
import com.sptp.backend.jwt.service.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

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
                                    @PathVariable Long artWorkId, @Valid @RequestBody ArtWorkBidRequest artWorkBidRequest){

        artWorkService.bid(userDetails.getMember().getId(), artWorkId, artWorkBidRequest.getPrice());

        return ResponseEntity.ok().build();
    }
}
