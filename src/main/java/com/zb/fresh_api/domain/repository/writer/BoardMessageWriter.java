package com.zb.fresh_api.domain.repository.writer;

import com.zb.fresh_api.domain.annotation.Writer;
import com.zb.fresh_api.domain.entity.board.BoardMessage;
import com.zb.fresh_api.domain.repository.jpa.BoardMessageJpaRepository;
import lombok.RequiredArgsConstructor;

@Writer
@RequiredArgsConstructor
public class BoardMessageWriter {

    private final BoardMessageJpaRepository boardMessageJpaRepository;

    public BoardMessage store(BoardMessage boardMessage) {
        return boardMessageJpaRepository.save(boardMessage);
    }
}
