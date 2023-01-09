package com.sptp.backend.member.web;

import com.sptp.backend.aws.service.AwsService;
import com.sptp.backend.jwt.service.dto.CustomUserDetails;
import com.sptp.backend.member.web.dto.request.*;
import com.sptp.backend.jwt.service.JwtService;
import com.sptp.backend.member.web.dto.response.*;
import com.sptp.backend.member.repository.Member;
import com.sptp.backend.member.service.MemberService;
import com.sptp.backend.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

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
    public ResponseEntity<MemberLoginResponseDto> login(@RequestBody MemberLoginRequestDto memberLoginRequestDto) {

        MemberLoginResponseDto memberLoginResponseDto = memberService.login(memberLoginRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(memberLoginResponseDto);
    }

    // 토큰 재발급
    @PostMapping("/members/token")
    public ResponseEntity<TokenResponseDto> refresh(@RequestBody Map<String, String> refreshToken) {

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
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String accessToken) {

        memberService.logout(accessToken);

        return new ResponseEntity(HttpStatus.OK);
    }

    // 아이디 찾기
    @PostMapping("/members/id")
    public ResponseEntity<Void> findId(@RequestBody Map<String, String> paramMap) throws Exception {

        String email = paramMap.get("email");
        Member member = memberService.findByEmail(email);
        emailService.sendIdMessage(member.getEmail());

        return new ResponseEntity(HttpStatus.OK);
    }

    // 비밀번호 임시 발급
    @PostMapping("/members/new-password")
    public ResponseEntity<Void> sendNewPassword(@RequestBody Map<String, String> paramMap) throws Exception {

        String email = paramMap.get("email");
        String newPassword = memberService.resetPassword(email);
        emailService.sendNewPasswordMessage(email, newPassword);

        return new ResponseEntity(HttpStatus.OK);
    }

    // 아이디 중복 체크
    @GetMapping("/members/check-id")
    public ResponseEntity<Void> checkUserId(@RequestParam("userId") String userId) {

        memberService.checkDuplicateMemberUserID(userId);

        return new ResponseEntity(HttpStatus.OK);
    }

    // 이메일 중복 체크
    @GetMapping("/members/check-email")
    public ResponseEntity<Void> checkUserEmail(@RequestParam("email") String email) {

        memberService.checkDuplicateMemberEmail(email);

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/members/check-nickname")
    public ResponseEntity<Void> checkUserNickname(@RequestParam("nickname") String nickname) {

        memberService.checkDuplicateMemberNickname(nickname);

        return new ResponseEntity(HttpStatus.OK);
    }

    // 작가 가입
    @PostMapping("artists/join")
    public ResponseEntity<ArtistSaveResponseDto> joinAuthor(@RequestParam(value = "image", required = false) MultipartFile image, ArtistSaveRequestDto artistSaveRequestDto) throws IOException {

        Member member = memberService.saveArtist(artistSaveRequestDto, image);

        ArtistSaveResponseDto artistSaveResponseDto = ArtistSaveResponseDto.builder()
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

        return ResponseEntity.status(HttpStatus.OK).body(artistSaveResponseDto);
    }

    // 비밀번호 재설정
    @PatchMapping("/members/password")
    public ResponseEntity<Void> changePassword(
            @RequestBody @Valid PasswordChangeRequest passwordChangeRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        memberService.changePassword(userDetails.getMember().getId(), passwordChangeRequest.getPassword());

        return new ResponseEntity(HttpStatus.OK);
    }

    // 회원 정보 수정
    @PatchMapping("/members")
    public ResponseEntity<Void> updateUser(@AuthenticationPrincipal CustomUserDetails userDetails,
                                        @RequestParam(value = "image", required = false) MultipartFile image,
                                        MemberUpdateRequest memberUpdateRequest) throws IOException {

        memberService.updateUser(userDetails.getMember().getId(), memberUpdateRequest, image);

        return new ResponseEntity(HttpStatus.OK);
    }

    // 회원 탈퇴
    @DeleteMapping("/members")
    public ResponseEntity<Void> withdrawUser(
            @RequestHeader("Authorization") String accessToken,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        memberService.logout(accessToken);
        memberService.withdrawUser(userDetails.getMember().getId());

        return new ResponseEntity(HttpStatus.OK);
    }

    // 작가 정보 수정
    @PatchMapping("/artists")
    public ResponseEntity<Void> updateArtist(@AuthenticationPrincipal CustomUserDetails userDetails,
                                          @RequestParam(value = "image", required = false) MultipartFile image,
                                          ArtistUpdateRequest artistUpdateRequest) throws IOException {

        memberService.updateArtist(userDetails.getMember().getId(), artistUpdateRequest, image);

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/members/me")
    // 회원 정보 조회
    public ResponseEntity<MemberResponse> getMember(@AuthenticationPrincipal CustomUserDetails userDetails){

        MemberResponse memberResponse = memberService.getMember(userDetails.getMember().getId());

        return ResponseEntity.status(HttpStatus.OK).body(memberResponse);
    }
}