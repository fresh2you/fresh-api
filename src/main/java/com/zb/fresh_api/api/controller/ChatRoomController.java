package com.zb.fresh_api.api.controller;

import com.zb.fresh_api.api.service.ChatRoomService;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.common.response.ApiResponse;
import com.zb.fresh_api.domain.entity.chat.ChatRoomMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "채팅방 관리 API",
        description = "채팅방 참여 요청 승인 및 요청 목록 조회 관련 API."
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @Operation(
            summary = "채팅방 참여 요청 승인",
            description = "판매자가 채팅방 참여 요청을 승인할 수 있습니다."
    )
    @PostMapping("/approve/{chatRoomId}/{memberId}")
    public ResponseEntity<ApiResponse<Void>> approveParticipant(
            @Parameter(description = "승인할 채팅방 ID", example = "1") @PathVariable @NotNull Long chatRoomId,
            @Parameter(description = "승인할 멤버 ID", example = "1001") @PathVariable @NotNull Long memberId) {

        chatRoomService.approveParticipant(chatRoomId, memberId);
        return ApiResponse.success(ResponseCode.SUCCESS, null); // 성공 응답 반환
    }

    @Operation(
            summary = "채팅방 참여 요청 목록 조회",
            description = "판매자가 채팅방에 참여 요청을 보낸 구매자들의 목록을 조회합니다."
    )
    @GetMapping("/requests/{chatRoomId}")
    public ResponseEntity<ApiResponse<List<ChatRoomMember>>> getPendingRequests(
            @Parameter(description = "조회할 채팅방 ID", example = "1") @PathVariable @NotNull Long chatRoomId) {

        List<ChatRoomMember> requests = chatRoomService.getPendingRequests(chatRoomId);
        return ApiResponse.success(ResponseCode.SUCCESS, requests); // 성공 응답에 요청 목록 포함
    }
}
