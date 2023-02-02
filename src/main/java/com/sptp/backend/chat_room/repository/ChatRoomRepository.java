package com.sptp.backend.chat_room.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByMemberIdAndArtistIdAndArtWorkId(Long memberId, Long artistId, Long artWorkId);

    @EntityGraph(attributePaths = {"artWork", "artist", "member"})
    List<ChatRoom> findAllByMemberIdOrArtistId(Long memberId, Long artistId);
}
