package com.zb.fresh_api.domain.entity.chat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public record ChatMessage(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long chatMessageId,
        Long chatRoomId,
        Long senderId,
        String message,
        LocalDateTime sentAt
) {
    public static ChatMessage create(Long chatRoomId, Long senderId, String message) {
        return new ChatMessage(null, chatRoomId, senderId, message, LocalDateTime.now());
    }
}
