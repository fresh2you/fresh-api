package com.zb.fresh_api.domain.repository.reader;

import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.domain.annotation.Reader;
import com.zb.fresh_api.domain.entity.board.Board;
import com.zb.fresh_api.domain.repository.jpa.BoardJpaRepository;
import com.zb.fresh_api.domain.repository.query.BoardQueryRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@Reader
@RequiredArgsConstructor
public class BoardReader {
    private final BoardJpaRepository boardJpaRepository;
    private final BoardQueryRepository boardQueryRepository;

    public boolean isExistBySameProduct(Long productId) {
        return boardJpaRepository.existsByProductId(productId);
    }

    public Board getById(final Long id) {
        return boardJpaRepository.findById(id)
            .orElseThrow(() -> new CustomException(ResponseCode.BOARD_NOT_FOUND));
    }

    public Optional<Board> findByProductId(final Long productId) {
        return boardJpaRepository.findByProductId(productId);
    }

    public List<Long> findProductIdsByMemberId(final Long id){
        return boardQueryRepository.findProductIdsByMemberId(id);
    }

}
