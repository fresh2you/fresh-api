package com.zb.fresh_api.domain.repository.reader;

import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.domain.annotation.Reader;
import com.zb.fresh_api.domain.entity.board.BoardMessage;
import com.zb.fresh_api.domain.repository.jpa.BoardMessageJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Reader
public class BoardMessageReader {
    private final BoardMessageJpaRepository boardMessageJpaRepository;

    public List<BoardMessage> findByBoardId(Long boardId){
        return boardMessageJpaRepository.findByBoardId(boardId);
    }

    public BoardMessage getById(final Long id) {
        return boardMessageJpaRepository.findById(id)
            .orElseThrow(() -> new CustomException(ResponseCode.BOARD_MESSAGE_NOT_FOUND));
    }
}
