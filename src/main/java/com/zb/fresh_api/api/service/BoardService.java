package com.zb.fresh_api.api.service;

import com.zb.fresh_api.api.dto.request.AddBoardMessageRequest;
import com.zb.fresh_api.api.dto.request.UpdateBoardRequest;
import com.zb.fresh_api.api.dto.response.AddBoardMessageResponse;
import com.zb.fresh_api.api.dto.response.AddBoardResponse;
import com.zb.fresh_api.api.dto.response.DeleteBoardMessageResponse;
import com.zb.fresh_api.api.dto.response.DeleteBoardResponse;
import com.zb.fresh_api.api.dto.response.GetAllBoardResponse;
import com.zb.fresh_api.api.dto.response.GetBoardMessagesResponse;
import com.zb.fresh_api.api.dto.response.UpdateBoardResponse;
import com.zb.fresh_api.api.utils.S3Uploader;
import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.domain.dto.file.UploadedFile;
import com.zb.fresh_api.domain.entity.board.Board;
import com.zb.fresh_api.domain.entity.board.BoardMessage;
import com.zb.fresh_api.domain.entity.member.Member;
import com.zb.fresh_api.domain.entity.product.Product;
import com.zb.fresh_api.domain.enums.board.MessageType;
import com.zb.fresh_api.domain.enums.category.CategoryType;
import com.zb.fresh_api.domain.repository.reader.BoardMessageReader;
import com.zb.fresh_api.domain.repository.reader.BoardReader;
import com.zb.fresh_api.domain.repository.reader.MemberReader;
import com.zb.fresh_api.domain.repository.reader.OrderReader;
import com.zb.fresh_api.domain.repository.reader.ProductReader;
import com.zb.fresh_api.domain.repository.writer.BoardMessageWriter;
import com.zb.fresh_api.domain.repository.writer.BoardWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardWriter boardWriter;
    private final BoardReader boardReader;
    private final MemberReader memberReader;
    private final ProductReader productReader;
    private final BoardMessageWriter boardMessageWriter;
    private final S3Uploader s3Uploader;
    private final OrderReader orderReader;
    private final BoardMessageReader boardMessageReader;

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

    public GetAllBoardResponse getAllBoard(final Long memberId) {
        Member member = memberReader.getById(memberId);

        List<Long> productIds = getProductIds(memberId, member);

        List<Board> boardResponse = new ArrayList<>();
        for(Long productId : productIds){
            Optional<Board> product = boardReader.findByProductId(productId);
            product.ifPresent(boardResponse::add);
        }

        return GetAllBoardResponse.fromEntities(boardResponse);
    }

    private List<Long> getProductIds(Long memberId, Member member) {
        List<Long> productIds = new ArrayList<>();
        if(member.isSeller()){
            productIds = boardReader.findProductIdsByMemberId(memberId);
        }else{
            productIds =  orderReader.findProductIdsByMemberId(memberId);
        }
        return productIds;
    }

    @Transactional
    public AddBoardMessageResponse addBoardMessage(final Long memberId, final Long boardId,final AddBoardMessageRequest request,
        final MultipartFile image) {
        Member member = memberReader.getById(memberId);
        Board board = boardReader.getById(boardId);
        
        // member가 구매자, 판매자 둘다 아닌경우 에러 발생
        if(!Objects.equals(board.getMember().getId(), memberId) &&
        !orderReader.existsByProductIdAndMemberId(board.getProduct().getId(), memberId)
        ){
            throw new CustomException(ResponseCode.NOT_BOARD_OWNER);
        }

        UploadedFile file = null;
        if(request.messageType() == MessageType.IMAGE && image != null){
             file = s3Uploader.upload(CategoryType.CHAT, image);
        }
        BoardMessage boardMessage = BoardMessage.create(board,request.messageType(),file == null  ? request.content() : file.url(),member);
        BoardMessage storeBoardMessage = boardMessageWriter.store(boardMessage);
        board.updateLastMessagedAt(storeBoardMessage.getCreatedAt());

        return new AddBoardMessageResponse(storeBoardMessage);
    }

    public GetBoardMessagesResponse getBoardMessageList(Long boardId, Long memberId) {
        Board board = boardReader.getById(boardId);
        Member member = memberReader.getById(memberId);
        if(!Objects.equals(board.getMember().getId(), memberId) &&
            !orderReader.existsByProductIdAndMemberId(board.getProduct().getId(), memberId)
        ){
            throw new CustomException(ResponseCode.NOT_BOARD_OWNER);
        }

        List<BoardMessage> boardMessageList = boardMessageReader.findByBoardId(boardId);

        return GetBoardMessagesResponse.fromEntities(boardMessageList, memberId);
    }

    @Transactional
    public DeleteBoardMessageResponse deleteBoardMessage(Long boardId, Long messageId, Long memberId) {
        Board board = boardReader.getById(boardId);
        Member member = memberReader.getById(memberId);
        if(!Objects.equals(board.getMember().getId(), memberId) &&
            !orderReader.existsByProductIdAndMemberId(board.getProduct().getId(), memberId)
        ){
            throw new CustomException(ResponseCode.NOT_BOARD_OWNER);
        }
        BoardMessage boardMessage = boardMessageReader.getById(messageId);
        boardMessage.delete();

        return new DeleteBoardMessageResponse(boardMessage.getId(),boardMessage.getDeletedAt());
    }
}
