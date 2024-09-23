package com.zb.fresh_api.api.controller;

import com.zb.fresh_api.api.annotation.LoginMember;
import com.zb.fresh_api.api.dto.request.AddBoardRequest;
import com.zb.fresh_api.api.dto.response.AddBoardResponse;
import com.zb.fresh_api.api.service.BoardService;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.common.response.ApiResponse;
import com.zb.fresh_api.domain.entity.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
    name = "게시판 API",
    description = "게시판 관련 API"
)
@RestController
@RequestMapping("/v1/api/boards")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @Operation(
        summary = "게시판 등록",
        description = "게시판을 등록합니다."
    )
    @PostMapping()
    public ResponseEntity<ApiResponse<AddBoardResponse>> addProduct(
        @Parameter(hidden = true) @LoginMember Member member, @RequestBody @Valid AddBoardRequest request) {
        AddBoardResponse response = boardService.addBoard(member.getId(), request.productId(), request.title());
        return ApiResponse.success(ResponseCode.SUCCESS, response);
    }

}
