package com.sptp.backend.controller.member;

import com.sptp.backend.controller.member.dto.request.MemberSaveRequestDto;
import com.sptp.backend.controller.member.dto.response.MemberSaveResponseDto;
import com.sptp.backend.domain.member.entity.Member;
import com.sptp.backend.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/members/save")
    public ResponseEntity<MemberSaveResponseDto> save(@RequestBody MemberSaveRequestDto memberSaveRequestDto) {

        Member member = memberService.saveUser(memberSaveRequestDto);

        MemberSaveResponseDto memberSaveResponseDto = new MemberSaveResponseDto(member.getUsername(), member.getEmail(), member.getAddress(), member.getTel());

        return ResponseEntity.status(HttpStatus.OK).body(memberSaveResponseDto);
    }


}
