package com.zb.fresh_api.domain.repository.jpa;

import com.zb.fresh_api.domain.entity.member.Member;
import com.zb.fresh_api.domain.enums.member.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberJpaRepository extends JpaRepository<Member, Long> {


    boolean existsByNicknameIgnoreCase(String nickname);

    boolean existsByEmailAndProvider(String email, Provider provider);

    boolean existsByNickname(String nickname);

    Optional<Member> findByEmailAndProvider(String email, Provider provider);

    Optional<Member> findByIdAndDeletedAtIsNull(Long id);

    Optional<Member> findByNickname(String nickname);
}
