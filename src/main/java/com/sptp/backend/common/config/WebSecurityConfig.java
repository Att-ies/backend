package com.sptp.backend.common.config;

import com.sptp.backend.jwt.web.CustomAccessDeniedHandler;
import com.sptp.backend.jwt.web.CustomAuthenticationEntryPoint;
import com.sptp.backend.jwt.web.JwtAuthenticationFilter;
import com.sptp.backend.jwt.web.JwtExceptionFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtExceptionFilter jwtExceptionFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // csrf 보안 토큰 disable처리.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 토큰 기반 인증이므로 세션 사용하지 x
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .and()
                .exceptionHandling()
                .accessDeniedHandler(customAccessDeniedHandler)
                .and()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .authorizeRequests() // 요청에 대한 사용권한 체크
                .antMatchers("/members/join", "/members/login", "/oauth2/*", "/members/token",
                        "/artists/join", "/members/id", "/members/new-password", "/members/check-email",
                        "/members/check-id", "/members/check-nickname", "/health-check").permitAll()
                .antMatchers("/ws-connection/**").permitAll() // 웹소켓 연결
                .antMatchers(HttpMethod.PATCH, "/members").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.PATCH, "/artists").hasAnyRole("ARTIST", "ADMIN")
                .antMatchers(HttpMethod.POST, "/art-works").hasAnyRole("ARTIST", "ADMIN")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated(); // 그외 나머지 요청은 인증 필요

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        // JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 전에 실행
        http.addFilterBefore(jwtExceptionFilter, jwtAuthenticationFilter.getClass());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedOrigin("https://atties.vercel.app");
        configuration.addAllowedOrigin("https://attiess.netlify.app");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}