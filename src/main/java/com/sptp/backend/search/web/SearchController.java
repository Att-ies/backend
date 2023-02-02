package com.sptp.backend.search.web;

import com.sptp.backend.jwt.service.dto.CustomUserDetails;
import com.sptp.backend.search.service.SearchService;
import com.sptp.backend.search.web.response.SearchWordResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<List<SearchWordResponse>> getRecentSearchWord(@AuthenticationPrincipal CustomUserDetails userDetails) {

        List<SearchWordResponse> recentSearchWord = searchService.getRecentSearchWord(userDetails.getMember());

        return ResponseEntity.status(HttpStatus.OK).body(recentSearchWord);
    }

    @DeleteMapping("/{wordId}")
    public ResponseEntity<Void> deleteSearchWord(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                 @PathVariable("wordId") Long wordId) {

        searchService.deleteSearchWord(wordId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteSearchWordAll(@AuthenticationPrincipal CustomUserDetails userDetails) {

        searchService.deleteSearchWordAll(userDetails.getMember());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
