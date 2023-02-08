package com.sptp.backend.member_ask_image.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberAskImageRepository extends JpaRepository<MemberAskImage, Long> {

    void deleteByMemberAskId(Long memberAskId);
}
