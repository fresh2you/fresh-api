package com.zb.fresh_api.api.service;

import com.zb.fresh_api.api.common.exception.CustomException;
import com.zb.fresh_api.api.common.exception.ErrorCode;
import com.zb.fresh_api.api.dto.TermsDto;
import com.zb.fresh_api.domain.entity.terms.Terms;
import com.zb.fresh_api.domain.repository.TermsRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TermsService {

    private final TermsRepository termsRepository;

    /**
     * 약관 목록 조회 로직
     * @return 목록ID, 약관명, 필수여부
     */
    public List<TermsDto> getTerms() {
        List<Terms> terms = termsRepository.findAll();

        return terms.stream().map(
            TermsDto::from
        ).toList();
    }

    public TermsDto getTerm(final Long termsId) {
        Terms terms = termsRepository.findById(termsId).orElseThrow(
            () -> new CustomException(ErrorCode.TERMS_NOT_FOUND)
        );
        return TermsDto.from(terms);
    }
}
