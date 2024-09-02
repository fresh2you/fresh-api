package com.zb.fresh_api.api.service;

import com.zb.fresh_api.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public boolean isNicknameAvailable(String nickname) {
        return !memberRepository.existsByNicknameIgnoreCase(nickname);
    }
}
