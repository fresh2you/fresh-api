package com.zb.fresh_api.api.controller;

import com.zb.fresh_api.api.annotation.LoginMember;
import com.zb.fresh_api.api.dto.request.AddBoardRequest;
import com.zb.fresh_api.api.dto.request.UpdateBoardRequest;
import com.zb.fresh_api.api.dto.response.AddBoardResponse;
import com.zb.fresh_api.api.dto.response.DeleteBoardResponse;
import com.zb.fresh_api.api.dto.response.UpdateBoardResponse;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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


    @Operation(
        summary = "게시판 수정",
        description = "게시판 수정을 위한 API입니다."
    )
    @PatchMapping(value = "/{boardId}")
    public ResponseEntity<ApiResponse<UpdateBoardResponse>> updateBoard(
        @PathVariable(name = "boardId") Long boardId,
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @Parameter @RequestBody @Valid UpdateBoardRequest request) {
        UpdateBoardResponse response = boardService.updateBoard(loginMember.getId(),
            request, boardId);
        return ApiResponse.success(ResponseCode.SUCCESS,response);
    }

    @Operation(
        summary = "게시판 삭제",
        description = "게시판 삭제를 위한 API입니다"
    )
    @DeleteMapping(value = "/{boardId}")
    public ResponseEntity<ApiResponse<DeleteBoardResponse>> deleteBoard(
        @PathVariable(name = "boardId") Long boardId,
        @Parameter(hidden = true) @LoginMember Member loginMember) {
        DeleteBoardResponse response = boardService.deleteBoard(loginMember.getId(),
            boardId);
        return ApiResponse.success(ResponseCode.SUCCESS,response);
    }

//    @Operation(
//        summary = "게시판 목록 조회",
//        description = "게시판 목록 조회를 위한 API입니다"
//    )
//    @PostMapping
//    public ResponseEntity<ApiResponse<Void>> getAllBoard(
//        @Parameter(hidden = true) @LoginMember Member loginMember) {
//            boardService.getAllBoard(loginMember.getId());
//        return ApiResponse.success(ResponseCode.SUCCESS);
//    }
}
