package com.sptp.backend.member.event;

import com.sptp.backend.common.NotificationCode;
import com.sptp.backend.member.repository.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberEvent {

    private final Member member;
    private final NotificationCode notificationCode;
}