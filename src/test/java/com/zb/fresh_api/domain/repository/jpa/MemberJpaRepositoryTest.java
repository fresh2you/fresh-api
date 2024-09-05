package com.zb.fresh_api.domain.repository.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import com.zb.fresh_api.domain.entity.member.Member;
import com.zb.fresh_api.domain.enums.member.MemberRole;
import com.zb.fresh_api.domain.enums.member.MemberStatus;
import com.zb.fresh_api.domain.enums.member.Provider;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberJpaRepositoryTest {

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Test
    @DisplayName("이메일로 검색 성공")
    void findByEmail_success() {
        // given
        Member member = Member.builder()
            .nickname("gin")
            .provider(Provider.EMAIL)
            .role(MemberRole.ROLE_USER)
            .status(MemberStatus.ACTIVE)
            .email("test@test.com").build();
        // when
        Member resultMember = memberJpaRepository.save(member);
        Optional<Member> byEmail = memberJpaRepository.findByEmail("test@test.com");
        // then
        assertThat(byEmail.get()).isEqualTo(member);
    }

    @Test
    @DisplayName("닉네임 중복 검사 실패(중복이 있음)")
    void existsByNicknameIgnoreCase_duplicateExists() {
        // given
        Member member = Member.builder()
            .nickname("gin")
            .provider(Provider.EMAIL)
            .role(MemberRole.ROLE_USER)
            .status(MemberStatus.ACTIVE)
            .email("test@test.com").build();
        // when
        Member resultMember = memberJpaRepository.save(member);
        boolean existsByNicknameIgnoreCase = memberJpaRepository.existsByNicknameIgnoreCase("Gin");
        // then
        assertThat(existsByNicknameIgnoreCase).isTrue();
    }

    @Test
    @DisplayName("이메일 중복검사 실패 (이메일을 통한 회원가입시 중복이 있음)")
    void existsByEmailAndProvider_duplicateExists() {
        // given
        Member member = Member.builder()
            .nickname("gin")
            .provider(Provider.EMAIL)
            .role(MemberRole.ROLE_USER)
            .status(MemberStatus.ACTIVE)
            .email("test@test.com").build();
        // when
        Member resultMember = memberJpaRepository.save(member);
        boolean existsByEmailAndProvider = memberJpaRepository.existsByEmailAndProvider(
            "test@test.com", Provider.EMAIL);
        //then
        assertThat(existsByEmailAndProvider).isTrue();
    }
}
