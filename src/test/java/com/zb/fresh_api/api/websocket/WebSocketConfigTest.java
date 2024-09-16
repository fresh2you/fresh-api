package com.zb.fresh_api.api.websocket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.StompWebSocketEndpointRegistration;

import static org.mockito.Mockito.*;

class WebSocketConfigTest {

    @Mock
    private StompEndpointRegistry stompEndpointRegistry;

    @Mock
    private StompWebSocketEndpointRegistration stompWebSocketEndpointRegistration;

    @InjectMocks
    private WebSocketConfig webSocketConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testStompEndpointRegistration() {
        // StompEndpointRegistry의 addEndpoint 메서드가 StompWebSocketEndpointRegistration을 반환하도록 설정
        when(stompEndpointRegistry.addEndpoint("/ws")).thenReturn(stompWebSocketEndpointRegistration);

        // StompWebSocketEndpointRegistration의 setAllowedOrigins 및 withSockJS 메서드가 호출되도록 설정
        when(stompWebSocketEndpointRegistration.setAllowedOrigins(anyString())).thenReturn(stompWebSocketEndpointRegistration);

        // 실제 WebSocketConfig의 registerStompEndpoints 메서드 호출
        webSocketConfig.registerStompEndpoints(stompEndpointRegistry);

        // Mockito verify로 호출 여부 확인
        verify(stompEndpointRegistry, times(1)).addEndpoint("/ws");
        verify(stompWebSocketEndpointRegistration, times(1)).setAllowedOrigins("http://localhost:3000");
        verify(stompWebSocketEndpointRegistration, times(1)).withSockJS();
    }
}
