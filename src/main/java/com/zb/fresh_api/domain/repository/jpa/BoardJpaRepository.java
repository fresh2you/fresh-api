package com.zb.fresh_api.domain.repository.jpa;

import com.zb.fresh_api.domain.entity.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardJpaRepository extends JpaRepository<Board, Long> {
    boolean existsByProductId(Long productId);
}