package com.zb.fresh_api.api.service;

import com.zb.fresh_api.api.common.exception.CustomException;
import com.zb.fresh_api.api.common.exception.ErrorCode;
import com.zb.fresh_api.domain.entity.member.Member;
import com.zb.fresh_api.domain.enums.member.MemberRole;
import com.zb.fresh_api.domain.enums.member.MemberStatus;
import com.zb.fresh_api.domain.enums.member.Provider;
import com.zb.fresh_api.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    public void nickNameValidate(String nickname) {
        boolean existsByNicknameIgnoreCase = memberRepository.existsByNicknameIgnoreCase(nickname);
        if(existsByNicknameIgnoreCase){
            throw new CustomException(ErrorCode.NICKNAME_ALREADY_IN_USE);
        }
    }

    public void emailValidate(String email) {
        boolean existsByEmailAndProvider = memberRepository.existsByEmailAndProvider(email, Provider.EMAIL);
        if(existsByEmailAndProvider){
            throw new CustomException(ErrorCode.EMAIL_ALREADY_IN_USE);
        }
    }
    public void signUp(String email, String password, String nickName) {
        this.nickNameValidate(nickName);
        this.emailValidate(email);
        memberRepository.save(
            Member.builder()
                .nickname(nickName)
                .email(email)
                .password(passwordEncoder.encode(password))
                .provider(Provider.EMAIL)
                .role(MemberRole.ROLE_USER)
                .status(MemberStatus.ACTIVE)
                .build()
        );

    }
}
