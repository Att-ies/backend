package com.sptp.backend.art_work.event;

import com.sptp.backend.art_work.repository.ArtWork;
import com.sptp.backend.common.NotificationCode;
import com.sptp.backend.member.repository.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ArtWorkCreateEvent {

    private final Member member;
    private final ArtWork artwork;
    private final NotificationCode notificationCode;
}
