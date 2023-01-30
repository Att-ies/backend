package com.sptp.backend.common.Factory;

import com.sptp.backend.MockPasswordEncoder;
import com.sptp.backend.member.repository.Member;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MemberFactory {

    private static Long id = 1L;
    PasswordEncoder passwordEncoder = new MockPasswordEncoder();

    public Member createMember(String userId, String email) {
        return createMemberBuilder(userId, email, "ROLE_USER")
                .build();
    }

    public Member createArtist(String userId, String email) {
        return createMemberBuilder(userId, email, "ROLE_ARTIST")
                .education("홍익대학교 조형예술학과")
                .history("2022 조형예술학과 졸업전시")
                .description("자연과 공생하는 미래를 꿈꾸며, 자연의 모습을 모티브로 작업합니다.")
                .instagram("ara_22")
                .behance("behance")
                .image("imageUrl")
                .build();
    }

    private Member.MemberBuilder createMemberBuilder(String userId, String email, String role) {
        return Member.builder()
                .id(id++)
                .nickname("nickname")
                .userId(userId)
                .email(email)
                .password(passwordEncoder.encode("password"))
                .telephone("010-1111-1111")
                .roles(Collections.singletonList(role));
    }

    // TODO 추후 인증 관련 테스트 시 사용 고려
//    public Authentication createAuthentication(UserPrincipal userPrincipal) {
//        Authentication authentication = new Authentication() {
//            @Override
//            public boolean equals(Object another) {
//                return false;
//            }
//            @Override
//            public String toString() {
//                return null;
//            }
//            @Override
//            public int hashCode() {
//                return 0;
//            }
//            @Override
//            public String getName() {
//                return null;
//            }
//            @Override
//            public Collection<? extends GrantedAuthority> getAuthorities() {
//                return null;
//            }
//            @Override
//            public Object getCredentials() {
//                return null;
//            }
//            @Override
//            public Object getDetails() {
//                return null;
//            }
//            @Override
//            public Object getPrincipal() {
//                return userPrincipal;
//            }
//            @Override
//            public boolean isAuthenticated() {
//                return false;
//            }
//            @Override
//            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
//            }
//        };
//        return authentication;
//    }
//    public UserPrincipal createNaverUser(User user) {
//        Map<String, Object> attributes = new HashMap<>();
//        attributes.put("id", "123456789");
//        OAuth2UserInfo oAuth2UserInfo = new NaverUserInfo(attributes);
//        return new UserPrincipal(user, oAuth2UserInfo, "accessToken");
//    }
//    public UserPrincipal createKakaoUser(User user) {
//        Map<String, Object> attributes = new HashMap<>();
//        attributes.put("id", "123456789");
//        OAuth2UserInfo oAuth2UserInfo = new KakaoUserInfo(attributes);
//        return new UserPrincipal(user, oAuth2UserInfo, "accessToken");
//    }
//
//    private List<Profile> getProfiles() {
//        return Arrays.asList(Profile.builder()
//                .path("https://amazon.com/asd2.png")
//                .name("test")
//                .build());
//    }
}