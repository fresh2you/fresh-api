package com.zb.fresh_api.domain.repository.reader;

import com.zb.fresh_api.domain.annotation.Reader;
import com.zb.fresh_api.domain.entity.terms.Terms;
import com.zb.fresh_api.domain.repository.jpa.TermsJpaRepository;
import com.zb.fresh_api.domain.repository.query.TermsQueryRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@Reader
@RequiredArgsConstructor
public class TermsReader {
    private final TermsJpaRepository termsJpaRepository;
    private final TermsQueryRepository termsQueryRepository;

    public List<Terms> findAll(){
//        return termsJpaRepository.findAll();
        return termsQueryRepository.findAll();
    }

    public List<Terms> findAllByIsRequired(boolean isRequired){
        return termsJpaRepository.findAllByIsRequired(isRequired);
    }

    public Optional<Terms> findById(Long id){
        return  termsJpaRepository.findById(id);
    }


}
