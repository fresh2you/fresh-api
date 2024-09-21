package com.zb.fresh_api.domain.repository.reader;

import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.domain.annotation.Reader;
import com.zb.fresh_api.domain.entity.point.Point;
import com.zb.fresh_api.domain.repository.jpa.PointJpaRepository;
import lombok.RequiredArgsConstructor;

@Reader
@RequiredArgsConstructor
public class PointReader {
    private final PointJpaRepository pointJpaRepository;

    public Point getPointByMemberId(Long memberId) {
        return pointJpaRepository.findByMemberId(memberId).orElseThrow(
            () -> new CustomException(ResponseCode.POINT_NOT_FOUND)
        );
    }
}
