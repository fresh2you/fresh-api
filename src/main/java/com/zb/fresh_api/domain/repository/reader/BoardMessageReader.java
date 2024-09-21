package com.zb.fresh_api.domain.repository.reader;

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
}
