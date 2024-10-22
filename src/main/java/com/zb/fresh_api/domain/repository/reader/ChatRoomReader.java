package com.zb.fresh_api.domain.repository.reader;

import com.zb.fresh_api.domain.annotation.Reader;
import com.zb.fresh_api.domain.repository.jpa.ChatRoomJpaRepository;
import com.zb.fresh_api.domain.repository.query.ChatRoomQueryRepository;
import lombok.RequiredArgsConstructor;

@Reader
@RequiredArgsConstructor
public class ChatRoomReader {

    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final ChatRoomQueryRepository chatRoomQueryRepository;

}
