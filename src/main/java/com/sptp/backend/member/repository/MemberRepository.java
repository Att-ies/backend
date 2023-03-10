package com.sptp.backend.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByUserId(String userId);

    boolean existsByEmail(String email);

    boolean existsByUserId(String userId);

    boolean existsByNickname(String nickname);

}
