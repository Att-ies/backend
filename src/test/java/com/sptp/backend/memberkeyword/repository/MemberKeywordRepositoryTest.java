package com.sptp.backend.memberkeyword.repository;

import com.sptp.backend.common.config.DBConfig;
import com.sptp.backend.common.config.PropertyConfig;
import com.sptp.backend.member.repository.Member;
import com.sptp.backend.memberkeyword.MemberKeywordMap;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;

@DataJpaTest
@Import({DBConfig.class, PropertyConfig.class})
class MemberKeywordRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    MemberKeywordRepository memberKeywordRepository;

    @Nested
    class deleteByMemberIdTest {

        Member member = createMember();

        @BeforeEach
        void init() {
            // 테스트용 데이터 삽입
            entityManager.persist(member);
            for (int keywordId = 1; keywordId < 3; keywordId++) {
                entityManager.persist(createMemberKeyword(member, keywordId));
            }
        }

        @Test
        void success() {
            //given
            long memberId = member.getId();
            long count = memberKeywordRepository.count();

            //when
            memberKeywordRepository.deleteByMemberId(memberId);
            long currentCount = memberKeywordRepository.count();

            //then
            Assertions.assertThat(count).isNotEqualTo(currentCount);
            Assertions.assertThat(currentCount).isEqualTo(0L);
        }
    }

    private static MemberKeyword createMemberKeyword(Member member, int keywordId) {
        return MemberKeyword.builder()
                .member(member)
                .keywordId(keywordId)
                .build();
    }

    private static Member createMember() {

        return Member.builder()
                .userId("test")
                .password("12345678")
                .build();
    }
}