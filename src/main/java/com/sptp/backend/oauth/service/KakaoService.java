package com.sptp.backend.oauth.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sptp.backend.jwt.service.JwtService;
import com.sptp.backend.jwt.web.JwtTokenProvider;
import com.sptp.backend.jwt.web.dto.TokenDto;
import com.sptp.backend.member.repository.Member;
import com.sptp.backend.member.repository.MemberRepository;
import com.sptp.backend.oauth.dto.KakaoTokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtService jwtService;

    @Value("${application.spring.security.kakao.client-id}")
    String rest_api_key;

    @Value("${application.spring.security.kakao.redirect-uri}")
    String redirect_uri;

    public KakaoTokenDto getKakaoToken(String code) {

        String access_Token = "";
        String refresh_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        String result = null;
        String id_token = null;
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //POST 요청에 필요로 요구하는 파라미터 스트림 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=" + rest_api_key);
            sb.append("&redirect_uri=" + redirect_uri);
            System.out.println("code = " + code);
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            //결과 코드가 200이면 성공
            int responseCode = conn.getResponseCode();
            log.info("responseCode={}", responseCode);

            //요청을 통해 얻은 JSON타입의 Response 메세지 Read
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            // bearer 토큰 값만 추출(log에 찍히는 값의 이름은 id_Token)
            log.info("response body={}", result);
            String[] temp = result.split(",");
            id_token = temp[3].substring(11);
            log.info("idToken={}", id_token);

            // Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

            log.info("카카오 access token={}", access_Token);
            log.info("카카오 refresh token={}", refresh_Token);

            br.close();
            bw.close();
            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }

        KakaoTokenDto kakaoTokenDto = KakaoTokenDto.builder()
                .accessToken(access_Token)
                .refreshToken(refresh_Token)
                .idToken(id_token)
                .build();

        return kakaoTokenDto;
    }

    public TokenDto loginWithKakao(KakaoTokenDto kakaoTokenDto) throws IOException {

        //1.유저 정보를 요청할 url
        String reqURL = "https://kapi.kakao.com/v2/user/me";

        //2.access_token을 이용하여 사용자 정보 조회
        URL url = new URL(reqURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Authorization", "Bearer " + kakaoTokenDto.getAccessToken()); //전송할 header 작성, access_token전송

        //결과 코드가 200이라면 성공
        int responseCode = conn.getResponseCode();
        log.info("responseCode={}", responseCode);

        //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = "";
        String result = "";

        while ((line = br.readLine()) != null) {
            result += line;
        }

        log.info("response body={}", result);

        //Gson 라이브러리로 JSON파싱
        JsonElement element = JsonParser.parseString(result);

        Long id = element.getAsJsonObject().get("id").getAsLong();
        boolean hasEmail = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_email").getAsBoolean();
        //사용자 이름
        String nickname = element.getAsJsonObject().get("properties").getAsJsonObject().get("nickname").getAsString();
        //사용자 이메일
        String email = "";
        if (hasEmail) {
            email = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();
        }

        //DB에 해당 이메일 없을 경우 회원 가입 로직 실행
        if (!memberRepository.existsByEmail(email)) {

            Member member = Member.builder()
                    .email(email)
                    .password("")
                    .roles(Collections.singletonList("ROLE_USER"))
                    .build();

            memberRepository.save(member);

            TokenDto tokenDto = jwtTokenProvider.createToken(email, member.getRoles());
            jwtService.saveRefreshToken(tokenDto);

            return tokenDto;

        } else {

            Optional<Member> member = memberRepository.findByEmail(email);

            //DB에 해당 이메일 회원 정보 있을 경우 jwt token 생성해서 리턴
            TokenDto tokenDto = jwtTokenProvider.createToken(email, member.get().getRoles());
            jwtService.saveRefreshToken(tokenDto);

            return tokenDto;

        }

    }

}
