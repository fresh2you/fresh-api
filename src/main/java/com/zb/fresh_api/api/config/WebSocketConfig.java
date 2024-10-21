package com.zb.fresh_api.api.config;

import com.zb.fresh_api.api.principal.CustomPrincipalHandshakeHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .setHandshakeHandler(new CustomPrincipalHandshakeHandler());
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 메시지를 구독하는 경로를 설정 (해당 경로를 구독하는 Client 에게 메세지 전달)
        registry.enableSimpleBroker("/sub");
        // 서버로 메세지 전송 (Client 의 발송 요청 처리)
        registry.setApplicationDestinationPrefixes("/pub");
    }

}
