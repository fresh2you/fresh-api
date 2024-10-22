package com.zb.fresh_api.domain.repository.reader;

import com.zb.fresh_api.domain.annotation.Reader;
import com.zb.fresh_api.domain.repository.jpa.ChatRoomMemberJpaRepository;
import com.zb.fresh_api.domain.repository.query.ChatRoomMemberQueryRepository;
import lombok.RequiredArgsConstructor;

@Reader
@RequiredArgsConstructor
public class ChatRoomMemberReader {

    private final ChatRoomMemberJpaRepository chatRoomMemberJpaRepository;
    private final ChatRoomMemberQueryRepository chatRoomMemberQueryRepository;

}
