package com.zb.fresh_api.domain.repository;

import com.zb.fresh_api.domain.entity.member.Member;
import com.zb.fresh_api.domain.enums.member.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByNicknameIgnoreCase(String nickname);
    boolean existsByEmailAndProvider(String email, Provider provider);
}
