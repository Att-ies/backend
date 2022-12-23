package com.sptp.backend.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sptp.backend.common.ErrorCode;
import com.sptp.backend.common.ResponseDto;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        Object invalidJwt = request.getAttribute("INVALID_JWT");
        Object expiredJwt = request.getAttribute("EXPIRED_JWT");

        String result = "";
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        if (invalidJwt != null) {
            response.setStatus(400);
            ResponseDto<?> msg = ResponseDto.fail(ErrorCode.INVALID_TOKEN);
            result = objectMapper.writeValueAsString(msg);
        } else if (expiredJwt != null) {
            response.setStatus(401);
            ResponseDto<?> msg = ResponseDto.fail(ErrorCode.ACCESS_TOKEN_EXPIRED);
            result = objectMapper.writeValueAsString(msg);
        }
        response.getWriter().write(result);
    }
}
