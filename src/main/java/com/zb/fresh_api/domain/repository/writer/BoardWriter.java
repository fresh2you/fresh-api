package com.zb.fresh_api.domain.repository.writer;

import com.zb.fresh_api.domain.annotation.Writer;
import com.zb.fresh_api.domain.entity.board.Board;
import com.zb.fresh_api.domain.repository.jpa.BoardJpaRepository;
import lombok.RequiredArgsConstructor;

@Writer
@RequiredArgsConstructor
public class BoardWriter {
    private final BoardJpaRepository boardJpaRepository;

    public Board store(Board board) {
        return boardJpaRepository.save(board);
    }
}
