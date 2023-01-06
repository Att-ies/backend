package com.sptp.backend.member.web;

import com.sptp.backend.jwt.service.dto.CustomUserDetails;
import com.sptp.backend.member.web.dto.request.*;
import com.sptp.backend.jwt.service.JwtService;
import com.sptp.backend.member.web.dto.response.*;
import com.sptp.backend.member.repository.Member;
import com.sptp.backend.member.service.MemberService;
import com.sptp.backend.email.service.EmailService;
import com.sptp.backend.jwt.web.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;
    private final JwtService jwtService;

    // 회원가입
    @PostMapping("/members/join")
    public ResponseEntity<MemberSaveResponseDto> join(@RequestBody MemberSaveRequestDto memberSaveRequestDto) {

        Member member = memberService.saveUser(memberSaveRequestDto);

        MemberSaveResponseDto memberSaveResponseDto = MemberSaveResponseDto.builder()
                .nickname(member.getNickname())
                .userId(member.getUserId())
                .email(member.getEmail())
                .telephone(member.getTelephone())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(memberSaveResponseDto);
    }

    // 로그인
    @PostMapping("/members/login")
    public ResponseEntity<?> login(@RequestBody MemberLoginRequestDto memberLoginRequestDto) {

        TokenDto token = memberService.login(memberLoginRequestDto);

        TokenResponseDto tokenResponseDto = TokenResponseDto.builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(tokenResponseDto);

    }

    @PostMapping("/members/token")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> refreshToken) {

        //Refresh Token 검증
        String recreatedAccessToken = jwtService.validateRefreshToken(refreshToken.get("refreshToken"));

        //Access Token 재발급
        TokenResponseDto tokenResponseDto = TokenResponseDto.builder()
                .accessToken(recreatedAccessToken)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(tokenResponseDto);
    }

    // 로그아웃
    @PostMapping("/members/logout")
    public ResponseEntity logout(@RequestHeader("accessToken") String accessToken) {

        memberService.logout(accessToken);

        return new ResponseEntity(HttpStatus.OK);
    }

    // 아이디 찾기
    @PostMapping("/members/id")
    public ResponseEntity<?> findId(@RequestBody Map<String, String> paramMap) throws Exception {

        String email = paramMap.get("email");
        Member member = memberService.findByEmail(email);
        emailService.sendIdMessage(member.getEmail());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/members/new-password")
    public ResponseEntity<?> sendNewPassword(@RequestBody Map<String, String> paramMap) throws Exception {

        String email = paramMap.get("email");
        String newPassword = memberService.resetPassword(email);
        emailService.sendNewPasswordMessage(email, newPassword);

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/members/check-id")
    public ResponseEntity<?> checkUserId(@RequestParam("userId") String userId) {

        boolean isDuplicated = memberService.isDuplicateUserId(userId);
        CheckDuplicateResponse checkDuplicateResponse = CheckDuplicateResponse.builder().duplicate(isDuplicated).build();

        return ResponseEntity.status(HttpStatus.OK).body(checkDuplicateResponse);
    }

    @GetMapping("/members/check-email")
    public ResponseEntity<?> checkUserEmail(@RequestParam("email") String email) {

        boolean isDuplicated = memberService.isDuplicateEmail(email);
        CheckDuplicateResponse checkDuplicateResponse = CheckDuplicateResponse.builder().duplicate(isDuplicated).build();

        return ResponseEntity.status(HttpStatus.OK).body(checkDuplicateResponse);
    }

    @GetMapping("/members/check-nickname")
    public ResponseEntity<?> checkUserNickname(@RequestParam("nickname") String nickname) {

        boolean isDuplicated = memberService.isDuplicateNickname(nickname);
        CheckDuplicateResponse checkDuplicateResponse = CheckDuplicateResponse.builder().duplicate(isDuplicated).build();

        return ResponseEntity.status(HttpStatus.OK).body(checkDuplicateResponse);
    }

    @PostMapping("artists/join")
    public ResponseEntity<AuthorSaveResponseDto> joinAuthor(@RequestBody AuthorSaveRequestDto authorSaveRequestDto) {

        Member member = memberService.saveAuthor(authorSaveRequestDto);

        AuthorSaveResponseDto authorSaveResponseDto = AuthorSaveResponseDto.builder()
                .nickname(member.getNickname())
                .userId(member.getUserId())
                .email(member.getEmail())
                .telephone(member.getTelephone())
                .education(member.getEducation())
                .history(member.getHistory())
                .description(member.getDescription())
                .instagram(member.getInstagram())
                .behance(member.getBehance())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(authorSaveResponseDto);
    }

    @PatchMapping("/members/password")
    public ResponseEntity<?> changePassword(
            @RequestBody @Valid PasswordChangeRequest passwordChangeRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        memberService.changePassword(userDetails.getMember().getId(), passwordChangeRequest.getPassword());

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 회원 정보 수정
    @PatchMapping("/members")
    public ResponseEntity<?> updateUser(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody MemberUpdateRequest memberUpdateRequest) {

        memberService.updateUser(userDetails.getMember().getId(), memberUpdateRequest);

        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/members")
    public ResponseEntity<?> withdrawUser(
            @RequestHeader("accessToken") String accessToken,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        memberService.logout(accessToken);
        memberService.withdrawUser(userDetails.getMember().getId());

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 작가 정보 수정
    @PatchMapping("/artists")
    public ResponseEntity<?> updateArtist(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody ArtistUpdateRequest artistUpdateRequest) {

        memberService.updateArtist(userDetails.getMember().getId(), artistUpdateRequest);

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/members")
    public ResponseEntity<MemberResponse> getMember(@AuthenticationPrincipal CustomUserDetails userDetails){

        Member member = memberService.findById(userDetails.getMember().getId());

        MemberResponse memberResponse = MemberResponse.builder()
                .nickname(member.getNickname())
                .image(member.getImage())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(memberResponse);
    }
}