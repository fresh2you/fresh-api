package com.zb.fresh_api.domain.dto.chat;

public record ChatMessageDto(
        Long id,
        Long senderId,
        String name,
        String message
) {}
