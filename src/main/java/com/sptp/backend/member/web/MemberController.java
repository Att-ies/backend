package com.sptp.backend.member.web;

import com.sptp.backend.jwt.service.dto.CustomUserDetails;
import com.sptp.backend.member.web.dto.request.*;
import com.sptp.backend.jwt.service.JwtService;
import com.sptp.backend.member.web.dto.response.*;
import com.sptp.backend.member.repository.Member;
import com.sptp.backend.member.service.MemberService;
import com.sptp.backend.email.service.EmailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Api(tags = {"회원 관련 API"})
@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;
    private final JwtService jwtService;

    // 회원가입
    @ApiOperation(value = "로컬 회원 가입 API", notes = "로컬 개발 전용 회원 가입 API")
    @PostMapping("/members/join")
    public ResponseEntity<MemberSaveResponseDto> join(@RequestBody MemberSaveRequestDto memberSaveRequestDto) {

        Member member = memberService.saveUser(memberSaveRequestDto);

        MemberSaveResponseDto memberSaveResponseDto = MemberSaveResponseDto.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .userId(member.getUserId())
                .email(member.getEmail())
                .telephone(member.getTelephone())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(memberSaveResponseDto);
    }

    // 로그인
    @ApiOperation(value = "로컬 로그인 API", notes = "유저 아이디와 패스워드를 통해 로그인합니다.")
    @PostMapping("/members/login")
    public ResponseEntity<MemberLoginResponseDto> login(@RequestBody MemberLoginRequestDto memberLoginRequestDto) {

        MemberLoginResponseDto memberLoginResponseDto = memberService.login(memberLoginRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(memberLoginResponseDto);
    }

    // 토큰 재발급
    @ApiOperation(value = "토큰 재발급 API", notes = "JWT 토큰을 message body에 넣어 요청합니다.")
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
    @ApiOperation(value = "회원 로그아웃 API", notes = "JWT 토큰을 헤더에 넣어 요청합니다.")
    @PostMapping("/members/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String accessToken) {

        memberService.logout(accessToken);

        return new ResponseEntity(HttpStatus.OK);
    }

    // 아이디 찾기
    @ApiOperation(value = "아이디 찾기 API", notes = "message body에 email 값을 넣어 요청합니다.")
    @PostMapping("/members/id")
    public ResponseEntity<Void> findId(@RequestBody Map<String, String> paramMap) throws Exception {

        String email = paramMap.get("email");
        Member member = memberService.findByEmail(email);
        emailService.sendIdMessage(member.getEmail());

        return new ResponseEntity(HttpStatus.OK);
    }

    // 비밀번호 임시 발급
    @ApiOperation(value = "비밀번호 임시발급 API", notes = "message body에 email 값을 넣어 요청합니다.")
    @PostMapping("/members/new-password")
    public ResponseEntity<Void> sendNewPassword(@RequestBody Map<String, String> paramMap) throws Exception {

        String email = paramMap.get("email");
        String newPassword = memberService.resetPassword(email);
        emailService.sendNewPasswordMessage(email, newPassword);

        return new ResponseEntity(HttpStatus.OK);
    }

    // 아이디 중복 체크
    @ApiOperation(value = "아이디 중복 체크 API", notes = "URL 파라미터에 userId 값을 넣어 요청합니다.")
    @GetMapping("/members/check-id")
    public ResponseEntity<Void> checkUserId(@RequestParam("userId") String userId) {

        memberService.checkDuplicateMemberUserID(userId);

        return new ResponseEntity(HttpStatus.OK);
    }

    // 이메일 중복 체크
    @ApiOperation(value = "이메일 중복 체크 API", notes = "URL 파라미터에 email 값을 넣어 요청합니다.")
    @GetMapping("/members/check-email")
    public ResponseEntity<Void> checkUserEmail(@RequestParam("email") String email) {

        memberService.checkDuplicateMemberEmail(email);

        return new ResponseEntity(HttpStatus.OK);
    }

    // 닉네임 중복 체크
    @ApiOperation(value = "닉네임 중복 체크 API", notes = "URL 파라미터에 nickname 값을 넣어 요청합니다.")
    @GetMapping("/members/check-nickname")
    public ResponseEntity<Void> checkUserNickname(@RequestParam("nickname") String nickname) {

        memberService.checkDuplicateMemberNickname(nickname);

        return new ResponseEntity(HttpStatus.OK);
    }

    // 비밀번호 재설정
    @ApiOperation(value = "비밀번호 재설정 API", notes = "header에 JWT 토큰, message body에 password 값을 넣어 요청합니다.")
    @PatchMapping("/members/password")
    public ResponseEntity<Void> changePassword(
            @RequestBody @Valid PasswordChangeRequest passwordChangeRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        memberService.changePassword(userDetails.getMember().getId(), passwordChangeRequest.getPassword());

        return new ResponseEntity(HttpStatus.OK);
    }

    // 회원 탈퇴
    @ApiOperation(value = "회원 탈퇴 API", notes = "헤더에 JWT 토큰 값을 넣어 요청합니다.")
    @DeleteMapping("/members")
    public ResponseEntity<Void> withdrawUser(
            @RequestHeader("Authorization") String accessToken,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        memberService.logout(accessToken);
        memberService.withdrawUser(userDetails.getMember().getId());

        return new ResponseEntity(HttpStatus.OK);
    }

    // 회원 정보 수정
    // TODO : MultipartFile image를 Request Dto 안에 포함해야 Swagger에서 인식
    @ApiOperation(value = "회원 정보 수정 API", notes = "헤더에 JWT 토큰 값을 넣어 요청합니다.")
    @PatchMapping(value = "/members")
    public ResponseEntity<Void> updateUser(@AuthenticationPrincipal CustomUserDetails userDetails,
                                           @RequestParam(value = "image", required = false) MultipartFile image,
                                           MemberUpdateRequest memberUpdateRequest) throws IOException {

        memberService.updateUser(userDetails.getMember().getId(), memberUpdateRequest, image);

        return new ResponseEntity(HttpStatus.OK);
    }

    // 작가 정보 수정
    @ApiOperation(value = "작가 정보 수정 API", notes = "헤더에 JWT 토큰 값을 넣어 요청합니다.")
    @PatchMapping("/artists")
    public ResponseEntity<Void> updateArtist(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             @RequestParam(value = "image", required = false) MultipartFile image,
                                             ArtistUpdateRequest artistUpdateRequest) throws IOException {

        memberService.updateArtist(userDetails.getMember().getId(), artistUpdateRequest, image);

        return new ResponseEntity(HttpStatus.OK);
    }

    // 작가 인증 파일 보내기
    @ApiOperation(value = "작가 인증 파일 전송 API", notes = "헤더에 JWT 토큰 값을 넣어 요청합니다.")
    @PatchMapping("/members/certification")
    public ResponseEntity<Void> certifyArtist(@AuthenticationPrincipal CustomUserDetails userDetails,
                                              @RequestParam(value = "certificationImage", required = true) MultipartFile image) throws IOException {

        memberService.certifyArtist(userDetails.getMember().getId(), image);

        return new ResponseEntity(HttpStatus.OK);
    }

    // 작가 인증 파일 목록 조회
    @ApiOperation(value = "관리자 작가 인증 파일 조회 API", notes = "")
    @GetMapping("/admin/members/certification")
    public ResponseEntity<List<CertificationResponse>> certificationList() {

        List<CertificationResponse> certificationResponses = memberService.getCertificationList();

        return ResponseEntity.status(HttpStatus.OK).body(certificationResponses);
    }

    // roles 작가로 전환
    @ApiOperation(value = "작가 전환 API", notes = "URL에 memberId 값을 넣어 요청합니다.")
    @PatchMapping("/admin/roles/{memberId}")
    public ResponseEntity<RolesChangeResponse> changeToArtist(@PathVariable(value = "memberId") Long memberId) {

        String roles = memberService.changeToArtist(memberId);

        RolesChangeResponse rolesChangeResponse = RolesChangeResponse.builder()
                .roles(roles)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(rolesChangeResponse);
    }

    // 회원 정보 조회
    @ApiOperation(value = "회원 정보 조회 API", notes = "헤더에 JWT 토큰 값을 넣어 요청합니다.")
    @GetMapping("/members/me")
    public ResponseEntity<MemberResponse> getMember(@AuthenticationPrincipal CustomUserDetails userDetails) {

        MemberResponse memberResponse = memberService.getMember(userDetails.getMember().getId());

        return ResponseEntity.status(HttpStatus.OK).body(memberResponse);
    }

    // 회원-작가 픽 관계 등록 (작가 픽하기)
    @ApiOperation(value = "작가 픽하기 API", notes = "헤더에 JWT 토큰 값을 넣어 요청, URL에 artistId 값을 넣어 요청합니다.")
    @PostMapping("/members/preferred-artists/{artistId}")
    public ResponseEntity<Void> pickArtist(@AuthenticationPrincipal CustomUserDetails userDetails,
                                           @PathVariable(value = "artistId") Long artistId) {

        memberService.pickArtist(userDetails.getMember().getId(), artistId);

        return new ResponseEntity(HttpStatus.OK);
    }

    // 회원-작가 픽 관계 취소
    @ApiOperation(value = "작가 픽 취소 API", notes = "헤더에 JWT 토큰 값을 넣어 요청, URL에 artistId 값을 넣어 요청합니다.")
    @DeleteMapping("/members/preferred-artists/{artistId}")
    public ResponseEntity<Void> deletePickArtist(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                 @PathVariable(value = "artistId") Long artistId) {

        memberService.deletePickArtist(userDetails.getMember().getId(), artistId);

        return new ResponseEntity(HttpStatus.OK);
    }

    // 회원-작가 픽 목록 조회
    @ApiOperation(value = "작가 픽 목록 조회 API", notes = "헤더에 JWT 토큰 값을 넣어 요청합니다.")
    @GetMapping("/members/preferred-artists")
    public ResponseEntity<List<PreferredArtistResponse>> preferredArtistList(@AuthenticationPrincipal CustomUserDetails userDetails) {

        List<PreferredArtistResponse> preferredArtistResponse = memberService.getPreferredArtistList(userDetails.getMember().getId());

        return ResponseEntity.status(HttpStatus.OK).body(preferredArtistResponse);
    }

    // 작가 상세 조회
    @ApiOperation(value = "작가 상세 조회 API", notes = "헤더에 JWT 토큰 값을 넣어 요청합니다, URL에 artistId 값을 넣어 요청합니다.")
    @GetMapping("/artists/{artistId}")
    public ResponseEntity<ArtistDetailResponse> getArtistDetail(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                @PathVariable(value = "artistId") Long artistId) {

        return ResponseEntity.ok(memberService.getArtistDetail(userDetails.getMember().getId(), artistId));
    }

    // 회원-작품 찜 관계 등록 (작품 찜하기)
    @ApiOperation(value = "작품 찜하기 API", notes = "헤더에 JWT 토큰 값을 넣어 요청, URL에 artWorkId 값을 넣어 요청합니다.")
    @PostMapping("/members/preferred-artworks/{artWorkId}")
    public ResponseEntity<Void> pickArtWork(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @PathVariable(value = "artWorkId") Long artWorkId) {

        memberService.pickArtWork(userDetails.getMember().getId(), artWorkId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 회원-작품 찜 관계 취소
    @ApiOperation(value = "작품 찜 취소 API", notes = "헤더에 JWT 토큰 값을 넣어 요청, URL에 artWorkId 값을 넣어 요청합니다.")
    @DeleteMapping("/members/preferred-artworks/{artWorkId}")
    public ResponseEntity<Void> deletePickArtWork(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                  @PathVariable(value = "artWorkId") Long artWorkId) {

        memberService.deletePickArtWork(userDetails.getMember().getId(), artWorkId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 회원-작품 찜 목록 조회
    @ApiOperation(value = "작품 찜 목록 조회 API", notes = "헤더에 JWT 토큰 값을 넣어 요청합니다.")
    @GetMapping("/members/preferred-artworks")
    public ResponseEntity<List<PreferredArtWorkResponse>> preferredArtWorkList(@AuthenticationPrincipal CustomUserDetails userDetails) {

        List<PreferredArtWorkResponse> preferredArtWorkResponse = memberService.getPreferredArtWorkList(userDetails.getMember().getId());

        return ResponseEntity.status(HttpStatus.OK).body(preferredArtWorkResponse);
    }

    // 회원-작품 취향 맞춤 추천 목록 조회
    @ApiOperation(value = "추천 작품 목록 조회 API", notes = "헤더에 JWT 토큰 값을 넣어 요청, 쿼리 파라미터에 page, limit 값을 넣어 요청합니다.")
    @GetMapping("/members/customized-artworks")
    public ResponseEntity<CustomizedArtWorkResponse> customizedArtWorkList(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                           @RequestParam("page") Integer page, @RequestParam("limit") Integer limit) {

        CustomizedArtWorkResponse customizedArtWorkResponse = memberService.getCustomizedArtWorkList(userDetails.getMember().getId(), page, limit);

        return ResponseEntity.status(HttpStatus.OK).body(customizedArtWorkResponse);
    }

    // 일대일 문의
    @ApiOperation(value = "일대일 문의 등록 API", notes = "헤더에 JWT 토큰 값을 넣어 요청합니다.")
    @PostMapping("/members/ask")
    public ResponseEntity<Void> saveAsk(@AuthenticationPrincipal CustomUserDetails userDetails,
                                        MemberAskRequestDto memberAskRequestDto) throws IOException {

        memberService.saveAsk(userDetails.getMember().getId(), memberAskRequestDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 일대일 문의 수정
    @ApiOperation(value = "일대일 문의 수정 API", notes = "헤더에 JWT 토큰 값을 넣어 요청, URL에 askId 값을 넣어 요청합니다.")
    @PatchMapping("/members/ask/{askId}")
    public ResponseEntity<Void> updateAsk(@AuthenticationPrincipal CustomUserDetails userDetails,
                                          @PathVariable(value = "askId") Long askId,
                                          MemberAskRequestDto memberAskRequestDto) throws IOException {

        memberService.updateAsk(userDetails.getMember().getId(), askId, memberAskRequestDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 일대일 문의 삭제
    @ApiOperation(value = "일대일 문의 삭제 API", notes = "헤더에 JWT 토큰 값을 넣어 요청, URL에 askId 값을 넣어 요청합니다.")
    @DeleteMapping("/members/ask/{askId}")
    public ResponseEntity<Void> deleteAsk(@AuthenticationPrincipal CustomUserDetails userDetails,
                                          @PathVariable(value = "askId") Long askId) throws IOException {

        memberService.deleteAsk(userDetails.getMember().getId(), askId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 일대일 문의 목록 조회
    @ApiOperation(value = "일대일 문의 조회 API", notes = "헤더에 JWT 토큰 값을 넣어 요청합니다.")
    @GetMapping("/members/ask")
    public ResponseEntity<List<MemberAskResponse>> getAskList(@AuthenticationPrincipal CustomUserDetails userDetails) {

        List<MemberAskResponse> memberAskResponsesList = memberService.getAskList(userDetails.getMember().getId());

        return ResponseEntity.status(HttpStatus.OK).body(memberAskResponsesList);
    }

    // 관리자 일대일 문의 목록 조회
    @ApiOperation(value = "관리자 일대일 문의 조회 API", notes = "헤더에 JWT 토큰 값을 넣어 요청합니다.")
    @GetMapping("/admin/members/ask")
    public ResponseEntity<List<MemberAskResponse>> getAllAskList() {

        List<MemberAskResponse> memberAskResponsesList = memberService.getAllAskList();

        return ResponseEntity.status(HttpStatus.OK).body(memberAskResponsesList);
    }

    // 일대일 문의 답변
    @ApiOperation(value = "일대일 문의 답변 API", notes = "헤더에 JWT 토큰 값을 넣어 요청, URL에 askId 값을 넣어 요청합니다.")
    @PatchMapping("/admin/members/answer/{askId}")
    public ResponseEntity<Void> updateAnswer(@PathVariable(value = "askId") Long askId,
                                             @RequestBody MemberAnswerRequest memberAnswerRequest) {

        memberService.updateAnswer(askId, memberAnswerRequest);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 관심 키워드 수정
    @ApiOperation(value = "관심 키워드 수정 API", notes = "헤더에 JWT 토큰 값을 넣어 요청합니다.")
    @PatchMapping("/members/keywords")
    public ResponseEntity<List<MemberUpdateKeywordsResponseDto>> updateKeywords(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                @RequestBody MemberUpdateKeywordsRequestDto memberUpdateKeywordsRequestDto) {

        List<MemberUpdateKeywordsResponseDto> memberUpdateKeywordsResponseDto = memberService.updateKeyword(userDetails.getMember().getId(), memberUpdateKeywordsRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(memberUpdateKeywordsResponseDto);
    }
}