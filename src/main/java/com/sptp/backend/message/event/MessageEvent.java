package com.sptp.backend.message.event;

import com.sptp.backend.chat_room.repository.ChatRoom;
import com.sptp.backend.common.NotificationCode;
import com.sptp.backend.member.repository.Member;
import com.sptp.backend.message.repository.Message;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MessageEvent {

    private final ChatRoom chatRoom;
    private final Message message;
    private final NotificationCode notificationCode;
}
