package com.zb.fresh_api.api.dto.response;

import java.time.LocalDateTime;

public record DeleteBoardMessageResponse (
    Long messageId,
    LocalDateTime deletedAt
){}
