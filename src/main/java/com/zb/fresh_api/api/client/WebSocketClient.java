package com.zb.fresh_api.api.client;

import com.zb.fresh_api.api.websocket.ChatWebSocketHandler;

public interface WebSocketClient {

    void connect(String url, ChatWebSocketHandler handler);

    void sendMessage(String message);

    void disconnect();
}
