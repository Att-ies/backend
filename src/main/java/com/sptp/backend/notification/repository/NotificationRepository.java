package com.sptp.backend.notification.repository;

import com.sptp.backend.member.repository.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByMemberIdOrderByModifiedDateDesc(Long memberId);
    long countByMemberAndChecked(Member member, boolean checked);

    boolean existsByUnique(Long unique);
    Optional<Notification> findByUnique(Long unique);
}
