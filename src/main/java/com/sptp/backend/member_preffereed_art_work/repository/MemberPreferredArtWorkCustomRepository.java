package com.sptp.backend.member_preffereed_art_work.repository;

import com.sptp.backend.art_work.repository.ArtWork;

import java.util.List;

public interface MemberPreferredArtWorkCustomRepository {
    List<ArtWork> findPreferredArtWork(Long memberId);
}
