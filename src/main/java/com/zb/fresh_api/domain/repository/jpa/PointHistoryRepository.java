package com.zb.fresh_api.domain.repository.jpa;

import com.zb.fresh_api.domain.entity.point.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {

}
