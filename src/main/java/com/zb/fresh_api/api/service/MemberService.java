package com.zb.fresh_api.api.service;

import com.zb.fresh_api.api.common.exception.CustomException;
import com.zb.fresh_api.api.common.exception.ErrorCode;
import com.zb.fresh_api.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public void nickNameValidate(String nickname) {
        boolean existsByNicknameIgnoreCase = memberRepository.existsByNicknameIgnoreCase(nickname);
        if(existsByNicknameIgnoreCase){
            throw new CustomException(ErrorCode.NICKNAME_ALREADY_IN_USE);
        }
    }
}
