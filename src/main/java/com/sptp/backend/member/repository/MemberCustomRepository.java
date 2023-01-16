package com.sptp.backend.member.repository;

import com.sptp.backend.art_work.repository.ArtWork;

import java.util.List;

public interface MemberCustomRepository {

    List<ArtWork> findCustomizedArtWork(List<Integer> memberKeywordId, Integer page);
}
