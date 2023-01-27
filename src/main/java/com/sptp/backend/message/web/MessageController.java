package com.sptp.backend.message.web;

import com.sptp.backend.jwt.service.dto.CustomUserDetails;
import com.sptp.backend.message.service.MessageService;
import com.sptp.backend.message.web.dto.MessageRequest;
import com.sptp.backend.message.web.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/send")
    public void chat(@Valid MessageRequest messageRequest) {

        messageService.saveMessage(messageRequest);

        MessageResponse messageResponse = MessageResponse.builder()
                .chatRoomId(messageRequest.getChatRoomId())
                .message(messageRequest.getMessage())
                .build();

        simpMessagingTemplate.convertAndSend("/queue/chat-rooms/" + messageRequest.getChatRoomId(),
                messageResponse);
    }
}
