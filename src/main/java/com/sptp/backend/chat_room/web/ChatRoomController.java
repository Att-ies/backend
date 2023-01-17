package com.sptp.backend.chat_room.web;

import com.sptp.backend.chat_room.service.ChatRoomService;
import com.sptp.backend.chat_room.web.dto.ChatRoomCreateRequest;
import com.sptp.backend.chat_room.web.dto.ChatRoomDetailResponse;
import com.sptp.backend.chat_room.web.dto.ChatRoomResponse;
import com.sptp.backend.jwt.service.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat-rooms")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    // 채팅방 생성
    @PostMapping
    public ResponseEntity<Void> createChatRoom(@Valid @RequestBody ChatRoomCreateRequest chatRoomCreateRequest,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {

        long chatRoomId = chatRoomService.createChatRoom(userDetails.getMember().getId(),
                chatRoomCreateRequest.getArtistId(), chatRoomCreateRequest.getArtWorkId());

        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                .header(HttpHeaders.LOCATION, "/chat-rooms/" + chatRoomId).build();
    }

    // 채팅방 조회
    @GetMapping("/{chatRoomId}")
    public ResponseEntity<ChatRoomDetailResponse> getChatRoomDetail(@PathVariable Long chatRoomId) {

        return ResponseEntity.ok(chatRoomService.getChatRoomDetail(chatRoomId));
    }

    // 채팅방 목록 조회
    @GetMapping
    public ResponseEntity<List<ChatRoomResponse>> getChatRooms(@AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity.ok(chatRoomService.getChatRooms(userDetails.getMember().getId()));
    }

    // 채팅방 나가기
    @DeleteMapping("/{chatRoomId}")
    public ResponseEntity<Void> leaveChatRoom(@PathVariable Long chatRoomId) {

        chatRoomService.leaveChatRoom(chatRoomId);

        return ResponseEntity.ok().build();
    }
}
