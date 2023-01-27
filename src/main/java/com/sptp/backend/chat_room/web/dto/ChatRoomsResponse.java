package com.sptp.backend.chat_room.web.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ChatRoomsResponse {

    List<ChatRoomResponse> chatRooms;
}
