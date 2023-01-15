package com.sptp.backend.member.web;
import com.sptp.backend.common.BaseControllerTest;
import com.sptp.backend.member.repository.Member;
import com.sptp.backend.member.service.MemberService;
import com.sptp.backend.member.web.dto.request.MemberLoginRequestDto;
import com.sptp.backend.member.web.dto.request.MemberSaveRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;


@Slf4j
class MemberControllerTest extends BaseControllerTest {

    private static final String nickname = "nickname1";
    private static final String userId = "test1";
    private static final String email = "test1@gmail.com";
    private static final String password = "test1234";
    private static final String telephone = "01012341234";
    private static final List<String> keywords = Arrays.asList("유화","미디어아트","세련된");

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    MemberService memberService;

    // 인증 필요한 api 테스트 시 해당 유저 정보 사용
    @PostConstruct
    void settingAuthenticatedUser() {

        MemberSaveRequestDto dto = MemberSaveRequestDto.builder()
                .nickname("nickname2")
                .userId("test2")
                .email("test2@gmail.com")
                .password("test1234")
                .telephone("01012341234")
                .keywords(Arrays.asList("사진","파스텔","유화","미디어아트","세련된"))
                .build();

        memberService.saveUser(dto);
    }

    @Test
    @DisplayName("로컬 회원 가입 테스트")
    void saveMemberTest() throws Exception {

        // given
        MemberSaveRequestDto dto = MemberSaveRequestDto.builder()
                .nickname(nickname)
                .userId(userId)
                .email(email)
                .password(password)
                .telephone(telephone)
                .keywords(keywords)
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/members/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(dto))
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(MockMvcResultHandlers.print());

        // then
        resultActions
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("nickname").value(nickname))
                .andExpect(MockMvcResultMatchers.jsonPath("userId").value(userId))
                .andExpect(MockMvcResultMatchers.jsonPath("email").value(email))
                .andExpect(MockMvcResultMatchers.jsonPath("telephone").value(telephone));
    }

    @Test
    @DisplayName("로그인 테스트")
    void memberLoginTest() throws Exception {

        // given
        MemberSaveRequestDto memberSaveRequestDto = MemberSaveRequestDto.builder()
                .nickname(nickname)
                .userId(userId)
                .email(email)
                .password(password)
                .telephone(telephone)
                .keywords(keywords)
                .build();

        memberService.saveUser(memberSaveRequestDto);

        MemberLoginRequestDto memberLoginRequestDto = MemberLoginRequestDto.builder()
                .userId(userId)
                .password(password)
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(memberLoginRequestDto))
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(MockMvcResultHandlers.print());

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("회원 정보 조회 테스트")
    @WithUserDetails(value = "test2")
    public void getUserInfo() throws Exception {

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/members/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(MockMvcResultHandlers.print());

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("nickname").value("nickname2"))
                .andExpect(MockMvcResultMatchers.jsonPath("userId").value("test2"))
                .andExpect(MockMvcResultMatchers.jsonPath("email").value("test2@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("telephone").value("01012341234"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.keywords[0]").value("사진"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.keywords[1]").value("파스텔"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.keywords[2]").value("유화"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.keywords[3]").value("미디어아트"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.keywords[4]").value("세련된"));
    }


}