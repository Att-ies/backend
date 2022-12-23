package com.sptp.backend.domain.member.service;

import com.sptp.backend.controller.member.dto.request.MemberSaveRequestDto;
import com.sptp.backend.domain.member.entity.Member;
import com.sptp.backend.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member saveUser(MemberSaveRequestDto dto) {
        Member member = new Member(dto.getUsername(),
                dto.getEmail(),
                dto.getAddress(),
                dto.getTel());

        memberRepository.save(member);
        return member;
    }
}
