package com.sptp.backend.member.event;

import com.sptp.backend.member.repository.Member;
import lombok.Getter;

@Getter
public class MemberToArtistEvent {

    private final Member member;

    public MemberToArtistEvent(Member member) {
        this.member = member;
    }

}
