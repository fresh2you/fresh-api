package com.zb.fresh_api.api.websocket;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@Configuration
class WebSocketConfigTest {

    @Test
    void contextLoads() {
        assertDoesNotThrow(() -> new WebSocketConfig());
    }
}
