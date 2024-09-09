package com.zb.fresh_api.domain.repository.writer;

import com.zb.fresh_api.domain.annotation.Writer;
import com.zb.fresh_api.domain.entity.point.PointHistory;
import com.zb.fresh_api.domain.repository.jpa.PointHistoryRepository;
import com.zb.fresh_api.domain.repository.query.PointHistoryQueryRepository;
import lombok.RequiredArgsConstructor;

@Writer
@RequiredArgsConstructor
public class PointHistoryWriter {
    private final PointHistoryRepository pointHistoryRepository;
    private final PointHistoryQueryRepository pointHistoryQueryRepository;

    public PointHistory store(PointHistory pointHistory) {return pointHistoryRepository.save(pointHistory);}
}
