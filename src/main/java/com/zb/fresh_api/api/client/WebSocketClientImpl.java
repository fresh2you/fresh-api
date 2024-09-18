package com.zb.fresh_api.api.client;

import com.zb.fresh_api.api.websocket.ChatWebSocketHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

@Component
public class WebSocketClientImpl implements WebSocketClient {

    private final StandardWebSocketClient webSocketClient = new StandardWebSocketClient();
    private WebSocketSession session;

    // WebSocketClient 인터페이스의 메서드를 올바르게 구현
    @Override
    public void connect(String url, ChatWebSocketHandler handler) {
        // ChatWebSocketHandler가 WebSocketHandler로 캐스팅되는지 확인
        ListenableFuture<WebSocketSession> future = webSocketClient.doHandshake((WebSocketHandler) handler, url);
        future.addCallback(
                result -> this.session = result, // 성공 시 session 저장
                ex -> { throw new RuntimeException("WebSocket 연결 오류", ex); } // 실패 시 예외 처리
        );
    }

    @Override
    public void sendMessage(String message) {
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (Exception e) {
                throw new RuntimeException("WebSocket 메시지 전송 오류", e);
            }
        } else {
            throw new IllegalStateException("WebSocket 세션이 열려있지 않습니다.");
        }
    }

    @Override
    public void disconnect() {
        if (session != null) {
            try {
                session.close();
            } catch (Exception e) {
                throw new RuntimeException("WebSocket 세션 종료 오류", e);
            }
        }
    }
}
