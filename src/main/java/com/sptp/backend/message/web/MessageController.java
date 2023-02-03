package com.sptp.backend.message.web;

import com.sptp.backend.message.service.MessageService;
import com.sptp.backend.message.web.dto.ImageChatRequest;
import com.sptp.backend.message.web.dto.ImageChatResponse;
import com.sptp.backend.message.web.dto.MessageRequest;
import com.sptp.backend.message.web.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/send")
    public void chatText(@Valid MessageRequest messageRequest) {

        MessageResponse messageResponse = messageService.saveMessage(
                messageRequest.getSenderId(), messageRequest.getChatRoomId(), messageRequest.getMessage());

        simpMessagingTemplate.convertAndSend("/queue/chat-rooms/" + messageRequest.getChatRoomId(),
                messageResponse);
    }

    @MessageMapping("/send-image")
    public void chatImage(@Valid ImageChatRequest imageChatRequest) {

        MessageResponse messageResponse = messageService.saveImage(
                imageChatRequest.getSenderId(), imageChatRequest.getChatRoomId(), imageChatRequest.getEncodedImage());

        simpMessagingTemplate.convertAndSend("/queue/chat-rooms/" + imageChatRequest.getChatRoomId(),
                messageResponse);
    }
}
