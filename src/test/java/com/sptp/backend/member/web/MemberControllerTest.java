package com.sptp.backend.member.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sptp.backend.jwt.service.CustomUserDetailsService;
import com.sptp.backend.jwt.web.JwtAuthenticationFilter;
import com.sptp.backend.jwt.web.JwtTokenProvider;
import com.sptp.backend.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = MemberController.class)
@ContextConfiguration(classes = {JwtTokenProvider.class, JwtAuthenticationFilter.class})
class MemberControllerTest {

    MockMvc mvc;

    @MockBean
    MemberService memberService;

    @MockBean
    RedisTemplate redisTemplate;

    @MockBean
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @Test
    void changePassword() throws Exception {
        //given
        ObjectMapper mapper = new ObjectMapper();

        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("password", "12");

        doNothing().when(memberService).changePassword(anyLong(), anyString());

        //when

        //then
        mvc.perform(patch("/members/password")
//                .header("accessToken", "accessToken")
                        .header("Authorization", "1234")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(paramMap))
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk());
    }
}