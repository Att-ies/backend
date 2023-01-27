package com.sptp.backend.common;

import com.sptp.backend.common.exception.CustomException;
import com.sptp.backend.common.exception.ErrorCode;
import com.sptp.backend.jwt.web.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // websocket Authorization 헤더 위치는 http와 다르기 때문에 추가 구현
        if (accessor.getCommand() == StompCommand.CONNECT) {
            if (!jwtTokenProvider.validateToken(accessor.getFirstNativeHeader("Authorization"))) {
                throw new CustomException(ErrorCode.TOKEN_INVALID);
            }
        }

        return message;
    }
}
