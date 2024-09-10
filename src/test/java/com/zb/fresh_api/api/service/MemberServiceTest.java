package com.zb.fresh_api.api.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.zb.fresh_api.api.dto.TermsAgreementDto;
import com.zb.fresh_api.api.dto.request.OauthLoginRequest;
import com.zb.fresh_api.api.dto.response.OauthLoginResponse;
import com.zb.fresh_api.api.factory.OauthProviderFactory;
import com.zb.fresh_api.common.base.ServiceTest;
import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.domain.dto.member.KakaoAccount;
import com.zb.fresh_api.domain.dto.member.KakaoUser;
import com.zb.fresh_api.domain.dto.token.Token;
import com.zb.fresh_api.domain.entity.member.Member;
import com.zb.fresh_api.domain.entity.member.MemberTerms;
import com.zb.fresh_api.domain.entity.terms.Terms;
import com.zb.fresh_api.domain.enums.member.MemberRole;
import com.zb.fresh_api.domain.enums.member.MemberStatus;
import com.zb.fresh_api.domain.enums.member.Provider;
import com.zb.fresh_api.domain.repository.reader.MemberReader;
import com.zb.fresh_api.domain.repository.reader.TermsReader;
import com.zb.fresh_api.domain.repository.writer.MemberTermsWriter;
import com.zb.fresh_api.domain.repository.writer.MemberWriter;
import com.zb.fresh_api.domain.repository.writer.PointHistoryWriter;
import com.zb.fresh_api.domain.repository.writer.PointWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import net.jqwik.api.Arbitraries;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

@DisplayName("회원 비즈니스 테스트")
class MemberServiceTest extends ServiceTest {

    @Mock
    private MemberReader memberReader;

    @Mock
    private MemberWriter memberWriter;

    @Mock
    private MemberTermsWriter memberTermsWriter;

    @Mock
    private TermsReader termsReader;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private OauthProviderFactory oauthProviderFactory;

    @Mock
    private PointWriter pointWriter;

    @Mock
    private PointHistoryWriter pointHistoryWriter;
    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("회원 가입을 진행하지 않은 사용자가 소셜 로그인을 진행한다.")
    void oauthLogin_sign_up_false() {
        // given
        Provider provider = Arbitraries.of(Provider.class).sample();
        String accessToken = Arbitraries.strings().alpha().ofMinLength(40).ofMaxLength(60).sample();

        OauthLoginRequest request = getConstructorMonkey().giveMeBuilder(OauthLoginRequest.class)
                .set("code", Arbitraries.strings().alpha().ofMinLength(20).ofMaxLength(40))
                .set("redirectUri", Arbitraries.strings().alpha().ofMinLength(20).ofMaxLength(40))
                .set("provider", provider)
                .sample();

        KakaoAccount kakaoAccount = getConstructorMonkey().giveMeBuilder(KakaoAccount.class)
                .set("emailNeedsAgreement", Arbitraries.of(true, false))
                .set("isEmailValid", Arbitraries.of(true, false))
                .set("isEmailVerified", Arbitraries.of(true, false))
                .set("email", Arbitraries.strings().alpha().ofMinLength(4).ofMaxLength(8).map(param -> param + "@gmail.com"))
                .sample();

        KakaoUser oauthUser = getConstructorMonkey().giveMeBuilder(KakaoUser.class)
                .set("id", Arbitraries.strings().numeric().ofMinLength(1).ofMaxLength(4))
                .set("hasSignedUp", Arbitraries.of(true, false))
                .set("connectedAt", Arbitraries.of(LocalDateTime.now()).sample())
                .set("synchedAt", Arbitraries.of(LocalDateTime.now()).sample())
                .set("kakaoAccount", kakaoAccount)
                .sample();

        boolean isSignup = false;
        Token token = Token.emptyToken();

        doReturn(accessToken).when(oauthProviderFactory).getAccessToken(request.provider(), request.redirectUri(), request.code());
        doReturn(oauthUser).when(oauthProviderFactory).getOAuthUser(request.provider(), accessToken);
        doReturn(isSignup).when(memberReader).existsByEmailAndProvider(oauthUser.email(), request.provider());

        // when
        OauthLoginResponse response = memberService.oauthLogin(request);

        // then
        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.isSignup(), isSignup);
        Assertions.assertEquals(response.loginMember().email(), oauthUser.email());
        Assertions.assertEquals(response.loginMember().provider(), request.provider());
        Assertions.assertEquals(response.token(), token);

        verify(oauthProviderFactory).getAccessToken(request.provider(), request.redirectUri(), request.code());
        verify(oauthProviderFactory).getOAuthUser(request.provider(), accessToken);
        verify(memberReader).existsByEmailAndProvider(oauthUser.email(), request.provider());

    }

    @Test
    @DisplayName("닉네임 중복 검사 실패")
    void nickNameValidate_duplicateExists() {
        // given
        when(memberReader.existActiveNickname(anyString())).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> memberService.nickNameValidate("gin"))
            .isInstanceOf(CustomException.class)
            .hasMessage(ResponseCode.NICKNAME_ALREADY_IN_USE.getMessage());
    }

    @Test
    @DisplayName("이메일 중복 검사 실패")
    void emailValidate_duplicateExists() {
        // given
        when(memberReader.existsByEmailAndProvider(anyString(),
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

        when(memberReader.existActiveNickname(anyString())).thenReturn(false);
        when(memberReader.existsByEmailAndProvider(anyString(),
            any(Provider.class))).thenReturn(false);
        when(termsReader.findAllByIsRequired(true)).thenReturn(List.of(
            terms1, terms2
        ));
        when(termsReader.findById(anyLong())).thenReturn(Optional.of(terms1));

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(memberWriter.store(any(Member.class))).thenReturn(
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
        verify(memberWriter, times(1)).store(any(Member.class));
        verify(memberTermsWriter, times(2)).store(any(MemberTerms.class));
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

        when(memberReader.existActiveNickname(anyString())).thenReturn(false);
        when(memberReader.existsByEmailAndProvider(anyString(),
            any(Provider.class))).thenReturn(false);
        when(termsReader.findAllByIsRequired(true)).thenReturn(termsList);
        when(termsReader.findById(anyLong())).thenReturn(Optional.of(terms));
        // then
        assertThatThrownBy(() -> memberService.signUp("test@test.com", "password", "gin",
            requestTermsAgreementDtos, Provider.EMAIL, "1"))
            .isInstanceOf(CustomException.class)
            .hasMessage(ResponseCode.TERMS_MANDATORY_NOT_AGREED.getMessage());
    }
}

