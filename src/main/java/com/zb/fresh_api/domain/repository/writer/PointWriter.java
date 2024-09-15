package com.zb.fresh_api.domain.repository.writer;

import com.zb.fresh_api.domain.annotation.Writer;
import com.zb.fresh_api.domain.entity.point.Point;
import com.zb.fresh_api.domain.repository.jpa.PointJpaRepository;
import com.zb.fresh_api.domain.repository.query.PointQueryRepository;
import lombok.RequiredArgsConstructor;

@Writer
@RequiredArgsConstructor
public class PointWriter {
    private final PointJpaRepository pointJpaRepository;
    private final PointQueryRepository  pointQueryRepository;

    public Point store(Point point) {return pointJpaRepository.save(point);}
}
