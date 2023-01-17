package com.sptp.backend.art_work.web;

import com.sptp.backend.art_work.service.ArtWorkService;
import com.sptp.backend.art_work.web.dto.request.ArtWorkSaveRequestDto;
import com.sptp.backend.art_work.web.dto.response.ArtWorkInfoResponseDto;
import com.sptp.backend.jwt.service.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class ArtWorkController {

    private final ArtWorkService artWorkService;

    // 작품 등록
    @PostMapping("/art-work")
    public ResponseEntity<Void> saveArtWork(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             ArtWorkSaveRequestDto artWorkSaveRequestDto) throws IOException {

        artWorkService.saveArtWork(userDetails.getMember().getId(), artWorkSaveRequestDto);

        return new ResponseEntity(HttpStatus.OK);
    }

    // 작품 상세 조회
    @GetMapping("/art-work/{artWorkId}")
    public ResponseEntity<ArtWorkInfoResponseDto> getArtWork(@PathVariable("artWorkId") Long artWorkId) {

        ArtWorkInfoResponseDto artWorkInfoResponseDto = artWorkService.getArtWork(artWorkId);

        return ResponseEntity.status(HttpStatus.OK).body(artWorkInfoResponseDto);
    }
}
