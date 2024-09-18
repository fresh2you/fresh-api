package com.zb.fresh_api.domain.repository.reader;

import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.domain.annotation.Reader;
import com.zb.fresh_api.domain.entity.board.Board;
import com.zb.fresh_api.domain.repository.jpa.BoardJpaRepository;
import lombok.RequiredArgsConstructor;

@Reader
@RequiredArgsConstructor
public class BoardReader {
    private final BoardJpaRepository boardJpaRepository;

    public boolean isExistBySameProduct(Long productId) {
        return boardJpaRepository.existsByProductId(productId);
    }

    public Board getById(final Long id) {
        return boardJpaRepository.findById(id)
            .orElseThrow(() -> new CustomException(ResponseCode.BOARD_NOT_FOUND));
    }
}
