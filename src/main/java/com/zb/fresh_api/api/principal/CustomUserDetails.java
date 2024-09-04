package com.zb.fresh_api.api.principal;

import com.zb.fresh_api.domain.entity.member.Member;
import com.zb.fresh_api.domain.enums.member.MemberStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public record CustomUserDetails(Member member) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getRole().name());
        return Collections.singleton(grantedAuthority);
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return member.getStatus() != MemberStatus.WITHDRAWAL;
    }

    @Override
    public boolean isAccountNonLocked() {
        return member.getStatus() != MemberStatus.BLOCKED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return member.getStatus() == MemberStatus.ACTIVE;
    }
    
}
