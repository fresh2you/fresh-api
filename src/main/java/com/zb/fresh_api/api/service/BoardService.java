package com.zb.fresh_api.api.service;

import com.zb.fresh_api.api.dto.request.AddBoardMessageRequest;
import com.zb.fresh_api.api.dto.request.UpdateBoardRequest;
import com.zb.fresh_api.api.dto.response.AddBoardMessageResponse;
import com.zb.fresh_api.api.dto.response.AddBoardResponse;
import com.zb.fresh_api.api.dto.response.DeleteBoardResponse;
import com.zb.fresh_api.api.dto.response.UpdateBoardResponse;
import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.domain.entity.board.Board;
import com.zb.fresh_api.domain.entity.board.BoardMessage;
import com.zb.fresh_api.domain.entity.member.Member;
import com.zb.fresh_api.domain.entity.product.Product;
import com.zb.fresh_api.domain.repository.reader.BoardReader;
import com.zb.fresh_api.domain.repository.reader.MemberReader;
import com.zb.fresh_api.domain.repository.reader.ProductReader;
import com.zb.fresh_api.domain.repository.writer.BoardMessageWriter;
import com.zb.fresh_api.domain.repository.writer.BoardWriter;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardWriter boardWriter;
    private final BoardReader boardReader;
    private final MemberReader memberReader;
    private final ProductReader productReader;
    private final BoardMessageWriter boardMessageWriter;

    public AddBoardResponse addBoard(final Long memberId, final Long productId, final String title ) {
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

    public UpdateBoardResponse updateBoard(final Long memberId, final UpdateBoardRequest request, final Long boardId) {
        Member member = memberReader.getById(memberId);
        Board board = boardReader.getById(boardId);
        if(!Objects.equals(board.getMember().getId(), memberId)){
            throw new CustomException(ResponseCode.NOT_BOARD_OWNER);
        }

        board.update(request.title());
        Board storedBoard = boardWriter.store(board);

        return new UpdateBoardResponse(board.getId(),board.getTitle(),board.getUpdatedAt());

    }

    @Transactional
    public DeleteBoardResponse deleteBoard(final Long memberId, final Long boardId) {
        Member member = memberReader.getById(memberId);
        Board board = boardReader.getById(boardId);
        if(!Objects.equals(board.getMember().getId(), memberId)){
            throw new CustomException(ResponseCode.NOT_BOARD_OWNER);
        }

        board.delete();

        return new DeleteBoardResponse(board.getId(),board.getDeletedAt());
    }

    public void getAllBoard(final Long memberId) {
        Member member = memberReader.getById(memberId);
        
        // TODO order가 생성된 이후 로직 작성
        // Member의 OrderID검색 후 productId들의 게시판 기록 조회
    }

    @Transactional
    public AddBoardMessageResponse addBoardMessage(Long memberId, Long boardId, AddBoardMessageRequest request) {
        Member member = memberReader.getById(memberId);
        Board board = boardReader.getById(boardId);
        if(!Objects.equals(board.getMember().getId(), memberId)){
            throw new CustomException(ResponseCode.NOT_BOARD_OWNER);
        }

        BoardMessage boardMessage = BoardMessage.create(board,request.messageType(),request.content());
        BoardMessage storeBoardMessage = boardMessageWriter.store(boardMessage);
        board.updateLastMessagedAt(storeBoardMessage.getCreatedAt());

        return new AddBoardMessageResponse(storeBoardMessage);
    }
}
