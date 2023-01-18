package com.sptp.backend.member.event;

import com.sptp.backend.member.repository.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Async
@Transactional(readOnly = true)
@Component
public class MemberEventListener {

    @EventListener
    public void handleMemberToArtistEvent(MemberToArtistEvent memberToArtistEvent){

        Member member = memberToArtistEvent.getMember();
        log.info(member.getNickname() + " becomes artist.");
        // DB에 Notification 정보 저장
    }
}
