package com.sptp.backend.controller.member;

import com.sptp.backend.controller.member.dto.request.MemberSaveRequestDto;
import com.sptp.backend.controller.member.dto.response.MemberSaveResponseDto;
import com.sptp.backend.domain.member.entity.Member;
import com.sptp.backend.domain.member.service.MemberService;
import com.sptp.backend.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;
    HashMap<String, String> emailMap = new HashMap<>();

    @PostMapping("/members/save")
    public ResponseEntity<MemberSaveResponseDto> save(@RequestBody MemberSaveRequestDto memberSaveRequestDto) {

        Member member = memberService.saveUser(memberSaveRequestDto);

        MemberSaveResponseDto memberSaveResponseDto = new MemberSaveResponseDto(member.getUsername(), member.getEmail(), member.getAddress(), member.getTel());

        return ResponseEntity.status(HttpStatus.OK).body(memberSaveResponseDto);
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
