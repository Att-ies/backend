package com.sptp.backend.common.stomp;

import com.sptp.backend.chat_room.service.ChatRoomService;
import com.sptp.backend.chat_room_connection.service.ChatRoomConnectionService;
import com.sptp.backend.common.exception.CustomException;
import com.sptp.backend.common.exception.ErrorCode;
import com.sptp.backend.jwt.service.dto.CustomUserDetails;
import com.sptp.backend.jwt.web.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
@Slf4j
public class StompHandler implements ChannelInterceptor {
    private final JwtTokenProvider jwtTokenProvider;
    private final ChatRoomConnectionService chatRoomConnectionService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // websocket Authorization 헤더 위치는 http와 다르기 때문에 추가 구현
        if (accessor.getCommand() == StompCommand.CONNECT) {
            if (!jwtTokenProvider.validateToken(accessor.getFirstNativeHeader("Authorization"))) {
                throw new CustomException(ErrorCode.TOKEN_INVALID);
            }
            log.info("StompUser = {}, Authentication = {}", accessor.getUser(), SecurityContextHolder.getContext().getAuthentication());
        } else if (accessor.getCommand() == StompCommand.SUBSCRIBE
                && "enter".equals(accessor.getFirstNativeHeader("action"))) {

            // TODO 추후 redis 도입 고려
            Long chatRoomId = Optional.ofNullable(getChatRoomId(accessor.getDestination()))
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_VALID_URI));
            Long loginMemberId = getLoginMemberId(accessor.getFirstNativeHeader("Authorization"));

            chatRoomConnectionService.connect(accessor.getSessionId(), chatRoomId, loginMemberId);
        } else if (accessor.getCommand() == StompCommand.DISCONNECT) {

            chatRoomConnectionService.disconnect(accessor.getSessionId());
        }

        return message;
    }

    private Long getChatRoomId(String destination) {
        if (Strings.isBlank(destination)) {
            return null;
        }

        int lastIndex = destination.lastIndexOf('/');
        if (lastIndex == -1) {
            return null;
        }

        try {
            return Long.parseLong(destination.substring(lastIndex + 1));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Long getLoginMemberId(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new CustomException(ErrorCode.TOKEN_INVALID);
        }

        CustomUserDetails userDetails = (CustomUserDetails) jwtTokenProvider.getAuthentication(token).getPrincipal();
        return userDetails.getMember().getId();
    }
}
