package com.sptp.backend.member.web;

import com.sptp.backend.member.web.dto.request.MemberLoginRequestDto;
import com.sptp.backend.member.web.dto.request.MemberSaveRequestDto;
import com.sptp.backend.member.web.dto.response.MemberSaveResponseDto;
import com.sptp.backend.member.web.dto.response.TokenResponseDto;
import com.sptp.backend.member.repository.Member;
import com.sptp.backend.member.service.MemberService;
import com.sptp.backend.email.service.EmailService;
import com.sptp.backend.jwt.web.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;
    HashMap<String, String> emailMap = new HashMap<>();

    // 회원가입
    @PostMapping("/members/join")
    public ResponseEntity<MemberSaveResponseDto> join(@RequestBody MemberSaveRequestDto memberSaveRequestDto) {

        Member member = memberService.saveUser(memberSaveRequestDto);

        MemberSaveResponseDto memberSaveResponseDto = MemberSaveResponseDto.builder()
                .username(member.getUsername())
                .userId(member.getUserId())
                .email(member.getEmail())
                .address(member.getAddress())
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


    @PostMapping("/emailConfirm")
    public String emailConfirm(@RequestParam String email) throws Exception {

        String confirm = emailService.sendMessage(email);
        emailMap.put(email, confirm);
        return confirm;
    }

    @PostMapping("/emailConfirmCheck")
    public String emailCheck(@RequestParam String email, @RequestParam String code) {
        if (emailMap.get(email).equals(code)) {
            emailMap.remove(email);
            return "OK";
        }else{
            return "NO";
        }
    }



}
