package com.zb.fresh_api.api.service;

import com.zb.fresh_api.api.dto.request.UpdateBoardRequest;
import com.zb.fresh_api.api.dto.response.AddBoardResponse;
import com.zb.fresh_api.api.dto.response.UpdateBoardResponse;
import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.domain.entity.board.Board;
import com.zb.fresh_api.domain.entity.member.Member;
import com.zb.fresh_api.domain.entity.product.Product;
import com.zb.fresh_api.domain.repository.reader.BoardReader;
import com.zb.fresh_api.domain.repository.reader.MemberReader;
import com.zb.fresh_api.domain.repository.reader.ProductReader;
import com.zb.fresh_api.domain.repository.writer.BoardWriter;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardWriter boardWriter;
    private final BoardReader boardReader;
    private final MemberReader memberReader;
    private final ProductReader productReader;

    public AddBoardResponse addBoard(Long memberId, Long productId, String title ) {
        Member member = memberReader.getById(memberId);
        Product product = productReader.findById(productId).orElseThrow(
            () -> new CustomException(ResponseCode.PRODUCT_NOT_FOUND)
        );

        if(!Objects.equals(product.getMember().getId(), memberId)){
            throw new CustomException(ResponseCode.NOT_PRODUCT_OWNER);
        }
        if(boardReader.isExistBySameProduct(productId)){
            throw new CustomException(ResponseCode.BOARD_ALREADY_EXIST);
        }

        Board board = boardWriter.store(Board.create(member, product, title));

        return new AddBoardResponse(board.getId(), board.getTitle(), board.getCreatedAt());
    }

    public UpdateBoardResponse updateBoard(Long memberId, UpdateBoardRequest request, Long boardId) {
        Member member = memberReader.getById(memberId);
        Board board = boardReader.getById(boardId);
        if(!Objects.equals(board.getMember().getId(), memberId)){
            throw new CustomException(ResponseCode.NOT_PRODUCT_OWNER);
        }

        board.update(request.title());
        Board storedBoard = boardWriter.store(board);

        return new UpdateBoardResponse(board.getId(),board.getTitle(),board.getUpdatedAt());

    }
}
