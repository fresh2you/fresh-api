package com.zb.fresh_api.api.principal;

import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.domain.entity.member.Member;
import com.zb.fresh_api.domain.repository.reader.MemberReader;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberReader memberReader;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberReader.getByEmail(email);

        CustomUserDetails userDetails = new CustomUserDetails(member);
        validateAuthenticate(userDetails);

        return userDetails;
    }

    private void validateAuthenticate(CustomUserDetails member) {
        if (Objects.isNull(member)) {
            throw new CustomException(ResponseCode.INTERNAL_AUTHENTICATION_SERVICE);
        }
        validateEnabled(member);
        validateAccountExpired(member);
        validateAccountNonLocked(member);
        validateCredentialNonExpired(member);
    }

    private static void validateEnabled(CustomUserDetails member) {
        if(!member.isEnabled()){
            throw new CustomException(ResponseCode.DISABLE_ACCOUNT);
        }
    }

    private static void validateCredentialNonExpired(CustomUserDetails member) {
        if (!member.isCredentialsNonExpired()) {
            throw new CustomException(ResponseCode.NON_EXPIRED_ACCOUNT);
        }
    }

    private static void validateAccountNonLocked(CustomUserDetails member) {
        if (!member.isAccountNonLocked()) {
            throw new CustomException(ResponseCode.NON_EXPIRED_ACCOUNT);
        }
    }

    private static void validateAccountExpired(CustomUserDetails member) {
        if (!member.isAccountNonExpired()) {
            throw new CustomException(ResponseCode.NON_EXPIRED_ACCOUNT);
        }
    }

}
