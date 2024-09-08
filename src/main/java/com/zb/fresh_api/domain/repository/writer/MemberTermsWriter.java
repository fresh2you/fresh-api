package com.zb.fresh_api.domain.repository.writer;

import com.zb.fresh_api.domain.annotation.Writer;
import com.zb.fresh_api.domain.entity.member.MemberTerms;
import com.zb.fresh_api.domain.repository.jpa.MemberTermsJpaRepository;
import lombok.RequiredArgsConstructor;

@Writer
@RequiredArgsConstructor
public class MemberTermsWriter {

    private final MemberTermsJpaRepository memberTermsJpaRepository;

    public void store(MemberTerms memberTerms) {
        memberTermsJpaRepository.save(memberTerms);
    }
}
