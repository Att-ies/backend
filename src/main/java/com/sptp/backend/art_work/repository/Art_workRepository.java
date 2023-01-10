package com.sptp.backend.art_work.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface Art_workRepository extends JpaRepository<Art_work, Long> {

    void deleteByMemberId(Long memberId);
}
