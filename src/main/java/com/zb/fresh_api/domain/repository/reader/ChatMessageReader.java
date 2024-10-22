package com.zb.fresh_api.domain.repository.reader;

import com.zb.fresh_api.domain.annotation.Reader;
import com.zb.fresh_api.domain.repository.jpa.ChatMessageJpaRepository;
import com.zb.fresh_api.domain.repository.query.ChatMessageQueryRepository;
import lombok.RequiredArgsConstructor;

@Reader
@RequiredArgsConstructor
public class ChatMessageReader {

    private final ChatMessageJpaRepository chatMessageJpaRepository;
    private final ChatMessageQueryRepository chatMessageQueryRepository;

}
