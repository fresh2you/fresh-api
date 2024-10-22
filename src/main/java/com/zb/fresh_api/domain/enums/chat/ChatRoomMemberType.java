package com.zb.fresh_api.domain.enums.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChatRoomMemberType {

    HOST("방 생성자"),
    PARTICIPANT("방 참여자");

    private final String description;
}
