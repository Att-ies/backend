package com.sptp.backend.domain.member.service;

import com.sptp.backend.controller.member.dto.request.MemberLoginRequestDto;
import com.sptp.backend.controller.member.dto.request.MemberSaveRequestDto;
import com.sptp.backend.domain.member.entity.Member;
import com.sptp.backend.domain.member.repository.MemberRepository;
import com.sptp.backend.exception.CustomException;
import com.sptp.backend.exception.ErrorCode;
import com.sptp.backend.jwt.JwtTokenProvider;
import com.sptp.backend.jwt.dto.TokenDto;
import com.sptp.backend.jwt.repository.RefreshTokenRepository;
import com.sptp.backend.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    @Transactional
    public Member saveUser(MemberSaveRequestDto dto) {

        checkDuplicateMember(dto.getEmail());

        Member member = new Member(dto.getUsername(),
                dto.getEmail(),
                passwordEncoder.encode(dto.getPassword()),
                dto.getAddress(),
                dto.getTel(),
                Collections.singletonList("ROLE_USER"));

        memberRepository.save(member);
        return member;
    }

    public TokenDto login(MemberLoginRequestDto dto) {

        // 이메일 및 비밀번호 유효성 체크
        Member findMember = memberRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER, "가입되지 않은 E-MAIL 입니다."));
        if (!passwordEncoder.matches(dto.getPassword(), findMember.getPassword())) {
            throw new CustomException(ErrorCode.NOT_MATCH_PASSWORD, "잘못된 비밀번호입니다.");
        }


        TokenDto tokenDto = jwtTokenProvider.createToken(findMember.getEmail(), findMember.getRoles());
        jwtService.saveRefreshToken(tokenDto);

        return tokenDto;
    }

    public Optional<Member> findByEmail(String email) {
        Optional<Member> findUser = memberRepository.findByEmail(email);
        if (findUser.isPresent()) {
            return findUser;
        }
        return null;
    }

    public void checkDuplicateMember(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.EXIST_MEMBER, "이미 이메일이 존재합니다.");
        }
    }

    public boolean checkDuplicateMemberBoolean(String email) {
        if (memberRepository.existsByEmail(email)) {
            return true;
        }else{
            return false;
        }
    }

}
