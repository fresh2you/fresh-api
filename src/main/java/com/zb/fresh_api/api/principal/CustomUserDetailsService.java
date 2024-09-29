package com.zb.fresh_api.api.principal;

import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.domain.entity.member.Member;
import com.zb.fresh_api.domain.enums.member.Provider;
import com.zb.fresh_api.domain.repository.reader.MemberReader;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private static final String EMAIL_SEPARATOR = "|";
    private final MemberReader memberReader;

    // 이 때 받은 email은 sufix가 포함되어있음
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String[] emailAndProvider = email.split("\\" + EMAIL_SEPARATOR);

        Member member = memberReader.getByEmailAndProvider(emailAndProvider[0],
            Provider.valueOf(emailAndProvider[1].toUpperCase()));

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
