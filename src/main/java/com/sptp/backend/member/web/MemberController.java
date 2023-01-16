package com.sptp.backend.member.web;

import com.sptp.backend.art_work.repository.ArtWork;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    // 회원 탈퇴
    @DeleteMapping("/members")
    public ResponseEntity<Void> withdrawUser(
            @RequestHeader("Authorization") String accessToken,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        memberService.logout(accessToken);
        memberService.withdrawUser(userDetails.getMember().getId());

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

    // 작가 정보 수정
    @PatchMapping("/artists")
    public ResponseEntity<Void> updateArtist(@AuthenticationPrincipal CustomUserDetails userDetails,
                                          @RequestParam(value = "image", required = false) MultipartFile image,
                                          ArtistUpdateRequest artistUpdateRequest) throws IOException {

        memberService.updateArtist(userDetails.getMember().getId(), artistUpdateRequest, image);

        return new ResponseEntity(HttpStatus.OK);
    }

    // roles 작가로 전환
    @PatchMapping("/members/roles")
    public ResponseEntity<RolesChangeResponse> changeToArtist(@AuthenticationPrincipal CustomUserDetails userDetails) {

        String roles = memberService.changeToArtist(userDetails.getMember().getId());

        RolesChangeResponse rolesChangeResponse = RolesChangeResponse.builder()
                .roles(roles)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(rolesChangeResponse);
    }

    // 회원 정보 조회
    @GetMapping("/members/me")
    public ResponseEntity<MemberResponse> getMember(@AuthenticationPrincipal CustomUserDetails userDetails){

        MemberResponse memberResponse = memberService.getMember(userDetails.getMember().getId());

        return ResponseEntity.status(HttpStatus.OK).body(memberResponse);
    }

    // 회원-작가 픽 관계 등록 (작가 픽하기)
    @PostMapping("/members/preferred-artists/{artistId}")
    public ResponseEntity<Void> pickArtist(@AuthenticationPrincipal CustomUserDetails userDetails,
                                           @PathVariable(value = "artistId") Long artistId) {

        memberService.pickArtist(userDetails.getMember().getId(), artistId);

        return new ResponseEntity(HttpStatus.OK);
    }

    // 회원-작가 픽 관계 취소
    @DeleteMapping("/members/preferred-artists/{artistId}")
    public ResponseEntity<Void> deletePickArtist(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                 @PathVariable(value = "artistId") Long artistId) {

        memberService.deletePickArtist(userDetails.getMember().getId(), artistId);

        return new ResponseEntity(HttpStatus.OK);
    }

    // 회원-작가 픽 목록 조회
    @GetMapping("/members/preferred-artists")
    public ResponseEntity<List<PreferredArtistResponse>> preferredArtistList(@AuthenticationPrincipal CustomUserDetails userDetails) {

        List<PreferredArtistResponse> preferredArtistResponse = memberService.getPreferredArtistList(userDetails.getMember().getId());

        return ResponseEntity.status(HttpStatus.OK).body(preferredArtistResponse);
    }

    // 회원-작품 찜 관계 등록 (작품 찜하기)
    @PostMapping("/members/preferred-artworks/{artWorkId}")
    public ResponseEntity<Void> pickArtWork(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @PathVariable(value = "artWorkId") Long artWorkId) {

        memberService.pickArtWork(userDetails.getMember().getId(), artWorkId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 회원-작품 찜 관계 취소
    @DeleteMapping("/members/preferred-artworks/{artWorkId}")
    public ResponseEntity<Void> deletePickArtWork(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @PathVariable(value = "artWorkId") Long artWorkId) {

        memberService.deletePickArtWork(userDetails.getMember().getId(), artWorkId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 회원-작품 찜 목록 조회
    @GetMapping("/members/preferred-artworks")
    public ResponseEntity<List<PreferredArtWorkResponse>> preferredArtWorkList(@AuthenticationPrincipal CustomUserDetails userDetails) {

        List<PreferredArtWorkResponse> preferredArtWorkResponse = memberService.getPreferredArtWorkList(userDetails.getMember().getId());

        return ResponseEntity.status(HttpStatus.OK).body(preferredArtWorkResponse);
    }

    // 일대일 문의
    @PostMapping("/members/ask")
    public ResponseEntity<Void> saveAsk(@AuthenticationPrincipal CustomUserDetails userDetails,
                                        MemberAskRequestDto memberAskRequestDto) throws IOException {

        memberService.saveAsk(userDetails.getMember().getId(), memberAskRequestDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 일대일 문의 수정
    @PostMapping("/members/ask/{askId}")
    public ResponseEntity<Void> updateAsk(@AuthenticationPrincipal CustomUserDetails userDetails,
                                          @PathVariable(value = "askId") Long askId,
                                          MemberAskRequestDto memberAskRequestDto) throws IOException {

        memberService.updateAsk(userDetails.getMember().getId(), askId, memberAskRequestDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 일대일 문의 삭제
    @DeleteMapping("/members/ask/{askId}")
    public ResponseEntity<Void> deleteAsk(@AuthenticationPrincipal CustomUserDetails userDetails,
                                          @PathVariable(value = "askId") Long askId) throws IOException {

        memberService.deleteAsk(userDetails.getMember().getId(), askId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 일대일 문의 목록 조회
    @GetMapping("/members/ask")
    public ResponseEntity<List<MemberAskResponse>> getAskList(@AuthenticationPrincipal CustomUserDetails userDetails) {

        List<MemberAskResponse> memberAskResponsesList = memberService.getAskList(userDetails.getMember().getId());

        return ResponseEntity.status(HttpStatus.OK).body(memberAskResponsesList);
    }
}