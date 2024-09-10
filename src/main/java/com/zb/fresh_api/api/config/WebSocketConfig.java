package com.zb.fresh_api.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatHandler(), "/chat").setAllowedOrigins("*");
    }

    @Bean
    public TextWebSocketHandler chatHandler() {
        return new TextWebSocketHandler() {
            private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());

            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws IOException {
                sessions.add(session);
                System.out.println("New connection established: " + session.getId());
            }

            @Override
            protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
                System.out.println("Received message from " + session.getId() + ": " + message.getPayload());
                // 모든 연결된 세션에 메시지 전송
                for (WebSocketSession s : sessions) {
                    if (s.isOpen() && !s.getId().equals(session.getId())) {
                        s.sendMessage(new TextMessage(message.getPayload()));
                    }
                }
            }

            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws IOException {
                sessions.remove(session);
                System.out.println("Connection closed: " + session.getId());
            }
        };
    }
}
