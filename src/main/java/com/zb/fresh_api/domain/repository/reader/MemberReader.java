package com.zb.fresh_api.domain.repository.reader;

import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.domain.annotation.Reader;
import com.zb.fresh_api.domain.entity.member.Member;
import com.zb.fresh_api.domain.enums.member.Provider;
import com.zb.fresh_api.domain.repository.jpa.MemberJpaRepository;
import com.zb.fresh_api.domain.repository.query.MemberQueryRepository;
import lombok.RequiredArgsConstructor;

@Reader
@RequiredArgsConstructor
public class MemberReader {

    private final MemberJpaRepository memberJpaRepository;
    private final MemberQueryRepository memberQueryRepository;

    public Member getById(final Long id) {
        return memberJpaRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND_MEMBER));
    }

    public Member getByEmail(String email) {
        return memberJpaRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND_MEMBER));
    }

    public boolean existsByEmailAndProvider(String email, Provider provider) {
        return memberJpaRepository.existsByEmailAndProvider(email, provider);
    }

    public boolean existActiveEmail(String email) {
        return memberQueryRepository.existActiveEmail(email);
    }

    public boolean existActiveNickname(String nickname) {
        return memberQueryRepository.existActiveNickname(nickname);
    }

}
