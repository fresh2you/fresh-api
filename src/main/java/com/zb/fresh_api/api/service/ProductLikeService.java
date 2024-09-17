package com.zb.fresh_api.api.service;

import com.zb.fresh_api.domain.repository.reader.ProductLikeReader;
import com.zb.fresh_api.domain.repository.reader.ProductReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductLikeService {
    private final ProductLikeReader productLikeReader;
    private final ProductReader productReader;


}
