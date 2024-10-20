package com.zb.fresh_api.domain.enums.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChatRoomStatus {
    AVAILABLE("사용 가능"),
    ADMIN_DELETED("어드민 삭제"),
    OWNER_DELETED("채팅방 생성자 삭제")
    ;

    private final String description;
}
