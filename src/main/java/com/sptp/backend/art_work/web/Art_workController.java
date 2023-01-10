package com.sptp.backend.art_work.web;

import com.sptp.backend.art_work.service.Art_workService;
import com.sptp.backend.art_work.web.dto.request.Art_workSaveRequestDto;
import com.sptp.backend.jwt.service.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class Art_workController {

    private final Art_workService art_workService;

    @PostMapping("/art-work")
    public ResponseEntity<Void> postArt_work(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             Art_workSaveRequestDto art_workSaveRequestDto) throws IOException {

        art_workService.saveArt_work(userDetails.getMember().getId(), art_workSaveRequestDto);

        return new ResponseEntity(HttpStatus.OK);
    }
}
