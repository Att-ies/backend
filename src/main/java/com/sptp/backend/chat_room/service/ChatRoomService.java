package com.sptp.backend.chat_room.service;

import com.sptp.backend.art_work.repository.ArtWorkRepository;
import com.sptp.backend.aws.service.AwsService;
import com.sptp.backend.aws.service.FileManager;
import com.sptp.backend.chat_room.repository.ChatRoom;
import com.sptp.backend.chat_room_connection.repository.ChatRoomConnection;
import com.sptp.backend.chat_room_connection.repository.ChatRoomConnectionRepository;
import com.sptp.backend.chat_room.repository.ChatRoomRepository;
import com.sptp.backend.chat_room.web.dto.ChatRoomDetailResponse;
import com.sptp.backend.chat_room.web.dto.ChatRoomResponse;
import com.sptp.backend.common.exception.CustomException;
import com.sptp.backend.common.exception.ErrorCode;
import com.sptp.backend.member.repository.Member;
import com.sptp.backend.member.repository.MemberRepository;
import com.sptp.backend.message.repository.Message;
import com.sptp.backend.message.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final ArtWorkRepository artWorkRepository;
    private final MessageRepository messageRepository;
    private final FileManager fileManager;

    public long createChatRoom(Long loginMemberId, Long artistId, Long artWorkId) {

        Optional<ChatRoom> chatRoomOptional =
                chatRoomRepository.findByMemberIdAndArtistIdAndArtWorkId(loginMemberId, artistId, artWorkId);

        // 기존 채팅방이 존재하는 경우 해당 id 반환
        if (chatRoomOptional.isPresent()) {
            return chatRoomOptional.get().getId();
        }

        ChatRoom chatRoom = ChatRoom.builder()
                .member(memberRepository.findById(loginMemberId)
                        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER)))
                .artist(memberRepository.findById(artistId)
                        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ARTIST)))
                .artWork(artWorkRepository.findById(artWorkId)
                        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ARTWORK)))
                .build();

        return chatRoomRepository.save(chatRoom).getId();
    }

    public ChatRoomDetailResponse getChatRoomDetail(Long loginMemberId, Long chatRoomId) {

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHAT_ROOM));
        List<Message> messages = messageRepository.findAllByChatRoomOrderByIdAsc(chatRoom);
        messages.forEach(message -> message.read(loginMemberId));

        return ChatRoomDetailResponse.builder()
                .chatRoomId(chatRoomId)
                .artist(ChatRoomDetailResponse.MemberDto.from(chatRoom.getArtist()))
                .member(ChatRoomDetailResponse.MemberDto.from(chatRoom.getMember()))
                .messages(messages.stream()
                        .map(ChatRoomDetailResponse.MessageDto::from)
                        .collect(Collectors.toList()))
                .build();
    }

    public List<ChatRoomResponse> getChatRooms(Long loginMemberId) {

        List<ChatRoom> chatRooms = chatRoomRepository.findAllByMemberIdOrArtistId(loginMemberId, loginMemberId);
       Member member = memberRepository.findById(loginMemberId)
               .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        return chatRooms.stream().map(chatRoom -> ChatRoomResponse.builder()
                        .chatRoomId(chatRoom.getId())
                        .artWorkImage(fileManager.getFullPath(chatRoom.getArtWork().getMainImage()))
                        .unreadCount(getUnreadCount(chatRoom, member))
                        .otherMember(getOtherMemberDto(chatRoom.getOtherMember(loginMemberId)))
                        .lastMessage(getLastMessageDto(chatRoom))
                        .build())
                .collect(Collectors.toList());
    }

    private ChatRoomResponse.MessageDto getLastMessageDto(ChatRoom chatRoom) {
        return messageRepository.findFirstByChatRoomOrderByIdDesc(chatRoom)
                .map(ChatRoomResponse.MessageDto::from)
                .orElse(null);

    }

    private ChatRoomResponse.MemberDto getOtherMemberDto(Member otherMember) {
        return ChatRoomResponse.MemberDto.of(otherMember, fileManager.getFullPath(otherMember.getImage()));
    }

    private Integer getUnreadCount(ChatRoom chatRoom, Member member) {
        return messageRepository.countByChatRoomAndIsReadIsFalseAndSenderNot(chatRoom, member);
    }

    public void leaveChatRoom(Long chatRoomId) {

        if (chatRoomRepository.existsById(chatRoomId)) {
            chatRoomRepository.deleteById(chatRoomId);
        }
    }
}
