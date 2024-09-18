package com.zb.fresh_api.domain.repository.jpa;

import com.zb.fresh_api.domain.entity.board.BoardMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardMessageJpaRepository extends JpaRepository<BoardMessage, Long> {

}
