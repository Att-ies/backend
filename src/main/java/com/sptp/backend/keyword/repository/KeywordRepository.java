package com.sptp.backend.keyword.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    Keyword findByName(String name);
}
