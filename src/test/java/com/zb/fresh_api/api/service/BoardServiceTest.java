package com.zb.fresh_api.api.service;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;

import com.zb.fresh_api.api.dto.request.UpdateBoardRequest;
import com.zb.fresh_api.api.dto.response.AddBoardResponse;
import com.zb.fresh_api.api.dto.response.UpdateBoardResponse;
import com.zb.fresh_api.common.base.ServiceTest;
import com.zb.fresh_api.domain.entity.board.Board;
import com.zb.fresh_api.domain.entity.member.Member;
import com.zb.fresh_api.domain.entity.product.Product;
import com.zb.fresh_api.domain.repository.reader.BoardReader;
import com.zb.fresh_api.domain.repository.reader.MemberReader;
import com.zb.fresh_api.domain.repository.reader.ProductReader;
import com.zb.fresh_api.domain.repository.writer.BoardWriter;
import java.util.Objects;
import java.util.Optional;
import net.jqwik.api.Arbitraries;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@DisplayName("게시판 비즈니스 테스트")
class BoardServiceTest extends ServiceTest {

    @Mock
    private BoardWriter boardWriter;
    
    @Mock
    private BoardReader boardReader;
    
    @Mock
    private MemberReader memberReader;
    
    @Mock
    private ProductReader productReader;
    
    @InjectMocks
    private BoardService boardService;
    
    @Test
    @DisplayName("게시판 생성 성공")
    void adddBoard_success(){
        // given
        Member member = getMember();
        Product product = getProduct(member);
        String title = Arbitraries.strings().ofMaxLength(20).sample();
        Board board = getReflectionMonkey().giveMeBuilder(Board.class)
            .set("member", member)
            .set("product", product)
            .set("title" ,title)
            .sample();
        doReturn(member).when(memberReader).getById(member.getId());
        doReturn(Optional.of(product)).when(productReader).findById(product.getId());
        doReturn(false).when(boardReader).isExistBySameProduct(product.getId());
        doReturn(board).when(boardWriter).store(argThat(x-> x.getProduct().equals(product)
            && x.getMember() == member && x.getTitle().equals(title)));
        // when
        AddBoardResponse response = boardService.addBoard(member.getId(), product.getId(),
            title);

        // then
        assertNotNull(response);

    }

    @Test
    @DisplayName("게시판 수정 성공")
    void updateBoard_success(){
        UpdateBoardRequest request = getConstructorMonkey().giveMeBuilder(UpdateBoardRequest.class)
            .set("title" , Arbitraries.strings().ofMaxLength(20))
            .sample();
        Long memberId = Arbitraries.longs().greaterOrEqual(1L).sample();
        Long boardId = Arbitraries.longs().greaterOrEqual(1L).sample();

        Member member = getReflectionMonkey().giveMeBuilder(Member.class)
            .set("id" ,memberId)
            .sample();
        Product product = getProduct(member);
        Board board = getBoard(member,product);
        Board updatedBoard = getReflectionMonkey().giveMeBuilder(Board.class)
            .set("member", member)
            .set("product", product)
            .set("title", request.title())
            .sample();

        Board board1 = Board.create(member,product, request.title());
        doReturn(member).when(memberReader).getById(memberId);
        doReturn(board).when(boardReader).getById(boardId);
        doReturn(updatedBoard).when(boardWriter).store(
            argThat(x -> Objects.equals(x.getMember().getId(), board1.getMember().getId())
            && Objects.equals(x.getProduct().getId(), board1.getProduct().getId())
            && Objects.equals(x.getTitle(), board1.getTitle()))
        );

        // when
        UpdateBoardResponse response = boardService.updateBoard(memberId, request, boardId);

        // then
        assertNotNull(response);
        assertEquals(response.boardId(),board.getId());
        assertEquals(response.title(), request.title());
        assertNotNull(board.getUpdatedAt());
    }
}
