package com.zb.fresh_api.api.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.zb.fresh_api.api.dto.TermsAgreementDto;
import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.domain.entity.member.Member;
import com.zb.fresh_api.domain.entity.member.MemberTerms;
import com.zb.fresh_api.domain.entity.terms.Terms;
import com.zb.fresh_api.domain.enums.member.MemberRole;
import com.zb.fresh_api.domain.enums.member.MemberStatus;
import com.zb.fresh_api.domain.enums.member.Provider;
import com.zb.fresh_api.domain.repository.jpa.MemberJpaRepository;
import com.zb.fresh_api.domain.repository.jpa.MemberTermsJpaRepository;
import com.zb.fresh_api.domain.repository.jpa.TermsJpaRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberJpaRepository memberJpaRepository;

    @Mock
    private MemberTermsJpaRepository memberTermsJpaRepository;

    @Mock
    private TermsJpaRepository termsJpaRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("닉네임 중복 검사 실패")
    void nickNameValidate_duplicateExists() {
        // given
        when(memberJpaRepository.existsByNicknameIgnoreCase(anyString())).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> memberService.nickNameValidate("gin"))
            .isInstanceOf(CustomException.class)
            .hasMessage(ResponseCode.NICKNAME_ALREADY_IN_USE.getMessage());
    }

    @Test
    @DisplayName("이메일 중복 검사 실패")
    void emailValidate_duplicateExists() {
        // given
        when(memberJpaRepository.existsByEmailAndProvider(anyString(),
            any(Provider.class))).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> memberService.emailValidate("test@test.com", Provider.EMAIL))
            .isInstanceOf(CustomException.class)
            .hasMessage(ResponseCode.EMAIL_ALREADY_IN_USE.getMessage());
    }

    @Test
    @DisplayName("회원가입 성공")
    void signUp_success() {
        // given
        List<TermsAgreementDto> termsAgreementDtos = List.of(
            new TermsAgreementDto(1L, true),
            new TermsAgreementDto(2L, true)
        );
        Terms terms1 = Terms.builder().id(1L).title("필수 약관1").isRequired(true).build();
        Terms terms2 = Terms.builder().id(2L).title("필수 약관2").isRequired(true).build();

        when(memberJpaRepository.existsByNicknameIgnoreCase(anyString())).thenReturn(false);
        when(memberJpaRepository.existsByEmailAndProvider(anyString(),
            any(Provider.class))).thenReturn(false);
        when(termsJpaRepository.findAllByIsRequired(true)).thenReturn(List.of(
            terms1, terms2
        ));
        when(termsJpaRepository.findById(anyLong())).thenReturn(Optional.of(terms1));

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(memberJpaRepository.save(any(Member.class))).thenReturn(
            Member.builder()
                .id(1L)
                .nickname("gin")
                .email("test@test.com")
                .password("encodedPassword")
                .provider(Provider.EMAIL)
                .role(MemberRole.ROLE_USER)
                .status(MemberStatus.ACTIVE)
                .build()
        );

        // when
        memberService.signUp("test@test.com", "password", "gin", termsAgreementDtos,
            Provider.EMAIL, "1");

        // then
        verify(memberJpaRepository, times(1)).save(any(Member.class));
        verify(memberTermsJpaRepository, times(2)).save(any(MemberTerms.class));
    }

    @Test
    @DisplayName("회원가입 실패 (필수 약관 미동의)")
    void signUp_fail_mandatoryTermsNotAgreed() {
        // given
        List<TermsAgreementDto> requestTermsAgreementDtos = List.of(
            new TermsAgreementDto(1L, false), // 필수 약관 미동의
            new TermsAgreementDto(2L, true)
        );
        Terms terms = Terms.builder().id(1L).title("필수 약관1").isRequired(true).build();
        List<Terms> termsList = List.of(
            terms
        );

        when(memberJpaRepository.existsByNicknameIgnoreCase(anyString())).thenReturn(false);
        when(memberJpaRepository.existsByEmailAndProvider(anyString(),
            any(Provider.class))).thenReturn(false);
        when(termsJpaRepository.findAllByIsRequired(true)).thenReturn(termsList);
        when(termsJpaRepository.findById(anyLong())).thenReturn(Optional.of(terms));
        // then
        assertThatThrownBy(() -> memberService.signUp("test@test.com", "password", "gin",
            requestTermsAgreementDtos, Provider.EMAIL, "1"))
            .isInstanceOf(CustomException.class)
            .hasMessage(ResponseCode.TERMS_MANDATORY_NOT_AGREED.getMessage());
    }
}
