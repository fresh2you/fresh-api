package com.zb.fresh_api.api.service;

import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.domain.entity.member.Member;
import com.zb.fresh_api.domain.enums.member.MemberRole;
import com.zb.fresh_api.domain.enums.member.MemberStatus;
import com.zb.fresh_api.domain.enums.member.Provider;
import com.zb.fresh_api.domain.repository.jpa.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberJpaRepository memberJpaRepository;
    private final PasswordEncoder passwordEncoder;
    public void nickNameValidate(String nickname) {
        boolean existsByNicknameIgnoreCase = memberJpaRepository.existsByNicknameIgnoreCase(nickname);
        if(existsByNicknameIgnoreCase){
            throw new CustomException(ResponseCode.NICKNAME_ALREADY_IN_USE);
        }
    }

    public void emailValidate(String email) {
        boolean existsByEmailAndProvider = memberJpaRepository.existsByEmailAndProvider(email, Provider.EMAIL);
        if(existsByEmailAndProvider){
            throw new CustomException(ResponseCode.EMAIL_ALREADY_IN_USE);
        }
    }
    public void signUp(String email, String password, String nickName) {
        this.nickNameValidate(nickName);
        this.emailValidate(email);
        memberJpaRepository.save(
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
