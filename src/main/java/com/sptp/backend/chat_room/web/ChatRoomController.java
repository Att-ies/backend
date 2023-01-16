package com.sptp.backend.chat_room.web;

import com.sptp.backend.chat_room.service.ChatRoomService;
import com.sptp.backend.chat_room.web.dto.ChatRoomCreateRequest;
import com.sptp.backend.chat_room.web.dto.ChatRoomDetailResponse;
import com.sptp.backend.jwt.service.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat-rooms")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping
    public ResponseEntity<Void> createChatRoom(@Valid @RequestBody ChatRoomCreateRequest chatRoomCreateRequest,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {

        long chatRoomId = chatRoomService.createChatRoom(userDetails.getMember().getId(),
                chatRoomCreateRequest.getArtistId(), chatRoomCreateRequest.getArtWorkId());

        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                .header(HttpHeaders.LOCATION, "/chat-rooms/" + chatRoomId).build();
    }

    @GetMapping("/{chatRoomId}")
    public ResponseEntity<ChatRoomDetailResponse> getChatRoomDetail(@PathVariable Long chatRoomId) {

        return ResponseEntity.ok(chatRoomService.getChatRoomDetail(chatRoomId));
    }
}
