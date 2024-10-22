package com.zb.fresh_api.domain.enums.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChatRoomType {
    PRIVATE("개인 채팅방"),
    GROUP("단체 채팅방");

    private final String description;
}
