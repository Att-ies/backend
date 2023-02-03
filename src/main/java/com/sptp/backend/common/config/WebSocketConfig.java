package com.sptp.backend.common.config;

import com.sptp.backend.common.stomp.AgentWebSocketHandlerDecoratorFactory;
import com.sptp.backend.common.stomp.StompHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;
    private final AgentWebSocketHandlerDecoratorFactory agentWebSocketHandlerDecoratorFactory;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 전송자가 '/app/**' 경로로 메세지 전송
        registry.setApplicationDestinationPrefixes("/app");

        // '/queue/**' or '/topic/**' 경로로 송신되었을 때, 스프링 내장의 simplebroker가 메세지 처리
        // queue는 1:1, topic은 1:n 관계 시 관습적으로 사용
        registry.enableSimpleBroker("/queue", "/topic");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-connection")
                .setAllowedOrigins("http://localhost:3000", "http://localhost:8080", "https://atties.vercel.app",
                        "https://attiess.netlify.app")
                .withSockJS(); // websocket 미지원 브라우저를 위해 추가
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.setDecoratorFactories(agentWebSocketHandlerDecoratorFactory); // 메시지 전송 크기 늘리는 용도
    }
}
