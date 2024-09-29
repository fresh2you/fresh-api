package com.zb.fresh_api.api.controller;

import com.zb.fresh_api.api.service.ChatBlockService;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/chat/block")
@RequiredArgsConstructor
public class ChatBlockController {

    private final ChatBlockService chatBlockService;

    @Operation(summary = "사용자 차단", description = "판매자 또는 구매자가 상대방을 차단")
    @PostMapping
    public ResponseEntity<ApiResponse<Object>> blockUser(
            @Parameter(description = "차단한 사용자 ID") @RequestParam Long chatBlockerId,
            @Parameter(description = "차단당한 사용자 ID") @RequestParam Long chatBlockedId) {

        chatBlockService.blockUser(chatBlockerId, chatBlockedId);
        return ApiResponse.success(ResponseCode.SUCCESS);
    }

    @Operation(summary = "차단 해제", description = "차단된 사용자를 차단 해제")
    @DeleteMapping
    public ResponseEntity<ApiResponse<Object>> unblockUser(
            @Parameter(description = "차단 해제할 사용자 ID") @RequestParam Long chatBlockerId,
            @Parameter(description = "차단된 사용자 ID") @RequestParam Long chatBlockedId) {

        chatBlockService.unblockUser(chatBlockerId, chatBlockedId);
        return ApiResponse.success(ResponseCode.SUCCESS);
    }
}
