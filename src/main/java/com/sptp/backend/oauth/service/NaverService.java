package com.sptp.backend.oauth.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sptp.backend.jwt.service.JwtService;
import com.sptp.backend.jwt.web.JwtTokenProvider;
import com.sptp.backend.jwt.web.dto.TokenDto;
import com.sptp.backend.member.repository.Member;
import com.sptp.backend.member.repository.MemberRepository;
import com.sptp.backend.oauth.dto.NaverTokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtService jwtService;

    @Value("${application.spring.security.naver.client-id}")
    String client_id;

    @Value("${application.spring.security.naver.client-secret}")
    String client_secret;

    @Value("${application.spring.security.naver.redirect-uri}")
    String redirect_uri;

    public NaverTokenDto getNaverToken(String code, String state) {

        String access_Token = "";
        String refresh_Token = "";
        String reqURL = "https://nid.naver.com/oauth2.0/token";

        String result = null;

        try{
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=" + client_id);
            sb.append("&client_secret=" + client_secret);
            System.out.println("code = " + code);
            sb.append("&code=" + code);
            sb.append("&state=" + state);
            bw.write(sb.toString());
            bw.flush();

            //응답 코드가 200이면 성공
            int responseCode = conn.getResponseCode();
            log.info("responseCode={}", responseCode);

            //요청을 통해 얻은 JSON타입의 Response 메세지 Read
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            log.info("response body={}", result);

            // Gson 라이브러리에 포함된 클래스로 JSON파싱
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

            log.info("네이버 access token={}", access_Token);
            log.info("네이버 refresh token={}", refresh_Token);

            br.close();
            bw.close();
            conn.disconnect();

        }catch (Exception e){
            e.printStackTrace();
        }

        NaverTokenDto naverTokenDto = NaverTokenDto.builder()
                .accessToken(access_Token)
                .refreshToken(refresh_Token)
                .build();

        return naverTokenDto;
    }

    public TokenDto loginWithNaver(NaverTokenDto naverTokenDto) throws IOException {

        //회원 정보 요청 url
        String reqURL = "https://openapi.naver.com/v1/nid/me";

        //accessToken 통한 사용자 정보 조회
        URL url = new URL(reqURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Authorization", "Bearer " + naverTokenDto.getAccessToken()); //전송할 header 작성, access_token전송

        //결과 코드가 200이면 성공
        int responseCode = conn.getResponseCode();
        log.info("responseCode={}", responseCode);

        //요청을 통해 얻은 JSON타입의 Response 메세지 Read
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = "";
        String result = "";

        while ((line = br.readLine()) != null) {
            result += line;
        }

        log.info("response body={}", result);

        //Gson 라이브러리로 JSON파싱
        JsonElement element = JsonParser.parseString(result);

        String email = element.getAsJsonObject().get("response").getAsJsonObject().get("email").getAsString();

        //DB에 해당 이메일 없을 경우 회원 가입 로직 실행
        if (!memberRepository.existsByEmail(email)) {

            String uuid = UUID.randomUUID().toString().substring(0, 7);
            String randomNickName = "user_" + uuid;

            while (memberRepository.existsByNickname(randomNickName)) {
                uuid = UUID.randomUUID().toString().substring(0, 7);
                randomNickName = "user_" + uuid;
            }

            Member member = Member.builder()
                    .email(email)
                    .password("")
                    .nickname(randomNickName)
                    .roles(Collections.singletonList("ROLE_USER"))
                    .build();

            memberRepository.save(member);

            TokenDto tokenDto = jwtTokenProvider.createToken(email, member.getRoles());
            tokenDto.setGrantType(member.getRoles().get(0));
            jwtService.saveRefreshToken(tokenDto);

            return tokenDto;

        } else {

            Optional<Member> member = memberRepository.findByEmail(email);

            //DB에 해당 이메일 회원 정보 있을 경우 jwt token 생성해서 리턴
            TokenDto tokenDto = jwtTokenProvider.createToken(email, member.get().getRoles());
            tokenDto.setGrantType(member.get().getRoles().get(0));
            jwtService.saveRefreshToken(tokenDto);

            return tokenDto;

        }

    }
}
