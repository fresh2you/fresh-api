package com.zb.fresh_api.domain.repository.reader;

import com.zb.fresh_api.domain.annotation.Reader;
import com.zb.fresh_api.domain.repository.jpa.BoardJpaRepository;
import lombok.RequiredArgsConstructor;

@Reader
@RequiredArgsConstructor
public class BoardReader {
    private final BoardJpaRepository boardJpaRepository;

    public boolean isExistBySameProduct(Long productId) {
        return boardJpaRepository.existsByProductId(productId);
    }
}
