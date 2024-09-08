package com.zb.fresh_api.api.service;

import com.zb.fresh_api.api.dto.TermsDto;
import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.domain.entity.terms.Terms;
import com.zb.fresh_api.domain.repository.reader.TermsReader;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TermsService {

    private final TermsReader termsReader;

    /**
     * 약관 목록 조회 로직
     * @return 목록ID, 약관명, 필수여부
     */
    public List<TermsDto> getTerms() {
        List<Terms> terms = termsReader.findAll();

        return terms.stream().map(
            TermsDto::fromEntity
        ).toList();
    }

    public TermsDto getTerm(final Long termsId) {
        Terms terms = termsReader.findById(termsId).orElseThrow(
            () -> new CustomException(ResponseCode.TERMS_NOT_FOUND)
        );
        return TermsDto.fromEntity(terms);
    }
}
