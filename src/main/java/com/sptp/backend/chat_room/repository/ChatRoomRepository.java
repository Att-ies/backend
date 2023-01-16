package com.sptp.backend.chat_room.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByMemberIdAndArtistIdAndArtWorkId(Long artistId, Long MemberId, Long artWorkId);
}
