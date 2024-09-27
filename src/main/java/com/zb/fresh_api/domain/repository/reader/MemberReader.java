package com.zb.fresh_api.domain.repository.reader;

import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.domain.annotation.Reader;
import com.zb.fresh_api.domain.dto.member.MemberWithPoint;
import com.zb.fresh_api.domain.entity.member.Member;
import com.zb.fresh_api.domain.enums.member.Provider;
import com.zb.fresh_api.domain.repository.jpa.MemberJpaRepository;
import com.zb.fresh_api.domain.repository.query.MemberQueryRepository;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Reader
@RequiredArgsConstructor
public class MemberReader {

    private final MemberJpaRepository memberJpaRepository;
    private final MemberQueryRepository memberQueryRepository;

    public Member getById(final Long id) {
        return memberJpaRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND_MEMBER));
    }

    public Member getByEmail(String email) {
        return memberJpaRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND_MEMBER));
    }

    public boolean existsByEmailAndProvider(String email, Provider provider) {
        return memberJpaRepository.existsByEmailAndProvider(email, provider);
    }

    public MemberWithPoint getMemberWithPointByEmailAndProvider(String email, Provider provider) {
        final MemberWithPoint memberWithPoint = memberQueryRepository.findMemberWithPointByEmailAndProvider(email, provider);
        if (Objects.isNull(memberWithPoint)) {
            throw new CustomException(ResponseCode.NOT_FOUND_MEMBER);
        }

        return memberWithPoint;
    }

    public MemberWithPoint findMemberWithPointByEmailAndProvider(String email, Provider provider) {
        return memberQueryRepository.findMemberWithPointByEmailAndProvider(email, provider);
    }

    public boolean existActiveEmail(String email) {
        return memberQueryRepository.existActiveEmail(email);
    }

    public boolean existActiveNickname(String nickname) {
        return memberQueryRepository.existActiveNickname(nickname);
    }

    public boolean existsByPhone(String phone){
        return memberQueryRepository.existsByPhone(phone);
    }

//    public Member getByEmailAndProvider(String email, Provider provider) {
//        return memberJpaRepository.findByEmailAndProvider(email, provider).orElseThrow(
//            () -> new CustomException(ResponseCode.NOT_FOUND_MEMBER)
//        );
//    }
}
